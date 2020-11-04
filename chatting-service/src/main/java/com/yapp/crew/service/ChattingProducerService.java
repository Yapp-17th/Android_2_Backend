package com.yapp.crew.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.errors.AlreadyApprovedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.GuestApplyNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.errors.WrongGuestException;
import com.yapp.crew.domain.errors.WrongHostException;
import com.yapp.crew.domain.model.AppliedUser;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.AppliedUserRepository;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.MessageRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.type.MessageType;
import com.yapp.crew.json.BotMessages;
import com.yapp.crew.network.HttpResponseBody;
import com.yapp.crew.payload.ApplyRequestPayload;
import com.yapp.crew.payload.ApproveRequestPayload;
import com.yapp.crew.payload.ChatRoomRequestPayload;
import com.yapp.crew.payload.ChatRoomResponsePayload;
import com.yapp.crew.payload.MessageResponsePayload;
import com.yapp.crew.payload.WelcomeMessageRequestPayload;
import com.yapp.crew.producer.ChattingProducer;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ChattingProducerService {

	private final ChattingProducer chattingProducer;

  private final ChatRoomRepository chatRoomRepository;

  private final MessageRepository messageRepository;

  private final AppliedUserRepository appliedUserRepository;

  private final BoardRepository boardRepository;

  private final UserRepository userRepository;

	private final BotMessages botMessages;

	@Autowired
	public ChattingProducerService(
					ChattingProducer chattingProducer,
					ChatRoomRepository chatRoomRepository,
					MessageRepository messageRepository,
					AppliedUserRepository appliedUserRepository,
					BoardRepository boardRepository,
					UserRepository userRepository,
					BotMessages botMessages
	) {
		this.chattingProducer = chattingProducer;
		this.chatRoomRepository = chatRoomRepository;
		this.messageRepository = messageRepository;
		this.appliedUserRepository = appliedUserRepository;
		this.boardRepository = boardRepository;
		this.userRepository = userRepository;
		this.botMessages = botMessages;
	}

  public HttpResponseBody<ChatRoomResponsePayload> createChatRoom(ChatRoomRequestPayload chatRoomRequestPayload) throws JsonProcessingException {
    User host = userRepository.findUserById(chatRoomRequestPayload.getHostId())
            .orElseThrow(() -> new UserNotFoundException("Cannot find host user with id"));

    User guest = userRepository.findUserById(chatRoomRequestPayload.getGuestId())
            .orElseThrow(() -> new UserNotFoundException("Cannot find guest user"));

    Board board = boardRepository.findById(chatRoomRequestPayload.getBoardId())
            .orElseThrow(() -> new BoardNotFoundException("Cannot find board"));

		Optional<ChatRoom> chatRoom = chatRoomRepository.findByGuestIdAndBoardId(guest.getId(), board.getId());
		if (chatRoom.isPresent()) {
			return HttpResponseBody.buildChatRoomResponse(ChatRoomResponsePayload.buildChatRoomResponsePayload(chatRoom.get()), HttpStatus.OK.value());
		}

		ChatRoom newChatRoom = ChatRoom.buildChatRoom(host, guest, board);

    host.addChatRoomHost(newChatRoom);
    guest.addChatRoomGuest(newChatRoom);
    chatRoomRepository.save(newChatRoom);

    produceWelcomeBotMessage(newChatRoom.getId());
    return HttpResponseBody.buildChatRoomResponse(ChatRoomResponsePayload.buildChatRoomResponsePayload(newChatRoom), HttpStatus.CREATED.value());
  }

  public HttpResponseBody<List<ChatRoomResponsePayload>> receiveChatRooms(Long userId) {
  	List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId);
		return HttpResponseBody.buildChatRoomsResponse(ChatRoomResponsePayload.buildChatRoomResponsePayload(chatRooms, userId), HttpStatus.OK.value());
	}

  public HttpResponseBody<List<MessageResponsePayload>> receiveChatMessages(Long chatRoomId, Long userId) {
    List<Message> messages = messageRepository.findAllByChatRoomIdOrderByCreatedAt(chatRoomId);
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
						.orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

    boolean isHost = chatRoom.isSenderChatRoomHost(userId);
    Long firstUnreadChatMessageId = chatRoom.findFirstUnreadChatMessage(isHost);

		return HttpResponseBody.buildChatMessagesResponse(
						MessageResponsePayload.buildMessageResponsePayload(messageRepository, messages, isHost),
						HttpStatus.OK.value(),
						firstUnreadChatMessageId
		);
  }

  public void applyUser(ApplyRequestPayload applyRequestPayload) throws JsonProcessingException {
		chattingProducer.applyUser(applyRequestPayload);
	}

	@Transactional
	public HttpResponseBody<?> approveUser(ApproveRequestPayload approveRequestPayload) throws JsonProcessingException {
		Board board = boardRepository.findById(approveRequestPayload.getBoardId())
						.orElseThrow(() -> new BoardNotFoundException("Board not found"));

		User host = userRepository.findUserById(approveRequestPayload.getHostId())
						.orElseThrow(() -> new UserNotFoundException("User not found"));

		User guest = userRepository.findUserById(approveRequestPayload.getGuestId())
						.orElseThrow(() -> new UserNotFoundException("User not found"));

		ChatRoom chatRoom = chatRoomRepository.findById(approveRequestPayload.getChatRoomId())
						.orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

		if (!board.getUser().getId().equals(host.getId())) {
			throw new WrongHostException("This user is not a host for this board");
		}

		if (!chatRoom.getHost().getId().equals(host.getId())) {
			throw new WrongHostException("This user is not a host for this chat room");
		}

		if (!chatRoom.getGuest().getId().equals(guest.getId())) {
			throw new WrongGuestException("This user is not a guest for this chat room");
		}

		AppliedUser isApplied = appliedUserRepository.findByBoardIdAndUserId(board.getId(), guest.getId())
						.orElseThrow(() -> new GuestApplyNotFoundException("This user did not apply"));

		if (isApplied.getStatus().equals(AppliedStatus.APPROVED)) {
			throw new AlreadyApprovedException("This user is already approved");
		}

		isApplied.approveUser();
		appliedUserRepository.save(isApplied);

		board.increaseRecruitCount();
		boardRepository.save(board);

		chattingProducer.approveUser(approveRequestPayload);

		return HttpResponseBody.buildSuccessResponse(HttpStatus.OK.value(), "Successfully approved guest");
	}

  private void produceWelcomeBotMessage(Long chatRoomId) throws JsonProcessingException {
    User bot = userRepository.findUserById(-1L)
            .orElseThrow(() -> new UserNotFoundException("Cannot find chat bot"));

		WelcomeMessageRequestPayload welcomeMessageRequestPayload = WelcomeMessageRequestPayload.builder()
            .content(botMessages.getWelcomeMessage().replace("\"", ""))
            .type(MessageType.BOT_MESSAGE)
            .senderId(bot.getId())
            .chatRoomId(chatRoomId)
            .build();

    chattingProducer.sendWelcomeBotMessage(welcomeMessageRequestPayload);
  }
}
