package com.yapp.crew.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.MessageRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.type.MessageType;
import com.yapp.crew.json.BotMessages;
import com.yapp.crew.network.HttpResponseBody;
import com.yapp.crew.payload.ApplyRequestPayload;
import com.yapp.crew.payload.ChatRoomRequestPayload;
import com.yapp.crew.payload.ChatRoomResponsePayload;
import com.yapp.crew.payload.MessageResponsePayload;
import com.yapp.crew.payload.WelcomeMessageRequestPayload;
import com.yapp.crew.producer.ChattingProducer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChattingProducerService {

	private final ChattingProducer chattingProducer;

  private final ChatRoomRepository chatRoomRepository;

  private final MessageRepository messageRepository;

  private final BoardRepository boardRepository;

  private final UserRepository userRepository;

	private final BotMessages botMessages;

	@Autowired
	public ChattingProducerService(
					ChattingProducer chattingProducer,
					ChatRoomRepository chatRoomRepository,
					MessageRepository messageRepository,
					BoardRepository boardRepository,
					UserRepository userRepository,
					BotMessages botMessages
	) {
		this.chattingProducer = chattingProducer;
		this.chatRoomRepository = chatRoomRepository;
		this.messageRepository = messageRepository;
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
			return HttpResponseBody.buildChatRoomResponse(buildChatRoomResponsePayload(chatRoom.get()), HttpStatus.OK.value());
		}

		ChatRoom newChatRoom = ChatRoom.getBuilder()
						.withHost(host)
						.withGuest(guest)
						.withBoard(board)
						.build();

    host.addChatRoomHost(newChatRoom);
    guest.addChatRoomGuest(newChatRoom);
    chatRoomRepository.save(newChatRoom);

    produceWelcomeBotMessage(newChatRoom.getId());
    return HttpResponseBody.buildChatRoomResponse(buildChatRoomResponsePayload(newChatRoom), HttpStatus.CREATED.value());
  }

  public HttpResponseBody<List<ChatRoomResponsePayload>> receiveChatRooms(Long userId) {
  	List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId);
		return HttpResponseBody.buildChatRoomsResponse(buildChatRoomResponsePayload(chatRooms, userId), HttpStatus.OK.value());
	}

  public HttpResponseBody<List<MessageResponsePayload>> receiveChatMessages(Long chatRoomId, Long userId) {
    List<Message> messages = messageRepository.findAllByChatRoomIdOrderByCreatedAt(chatRoomId);
    boolean isHost = isSenderChatRoomHost(chatRoomId, userId);
    Long firstUnreadChatMessageId = findFirstUnreadChatMessage(messages, isHost);
		return HttpResponseBody.buildChatMessagesResponse(buildMessageResponsePayload(messages, isHost), HttpStatus.OK.value(), firstUnreadChatMessageId);
  }

  public void applyUser(ApplyRequestPayload applyRequestPayload) throws JsonProcessingException {
		chattingProducer.applyUser(applyRequestPayload);
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

  private boolean isSenderChatRoomHost(Long chatRoomId, Long userId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
						.orElseThrow(() -> new ChatRoomNotFoundException("Cannot find chat room"));

		return userId.equals(chatRoom.getHost().getId());
	}

  private Long findFirstUnreadChatMessage(List<Message> messages, boolean isHost) {
  	Optional<Message> firstUnreadChatMessage = null;

  	if (isHost) {
			firstUnreadChatMessage = messages.stream()
							.filter(message -> !message.isHostRead())
							.findFirst();
		}
  	else {
			firstUnreadChatMessage = messages.stream()
							.filter(message -> !message.isGuestRead())
							.findFirst();
		}

  	if (firstUnreadChatMessage.isPresent()) {
  		return firstUnreadChatMessage.get().getId();
		}
  	return -1L;
	}

	private long countUnreadMessages(List<Message> messages, boolean isHost) {
		if (isHost) {
			return messages.stream()
							.filter(message -> !message.isHostRead())
							.count();
		}
		return messages.stream()
						.filter(message -> !message.isGuestRead())
						.count();
	}

	private ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom) {
		return ChatRoomResponsePayload.builder()
						.id(chatRoom.getId())
						.hostId(chatRoom.getHost().getId())
						.guestId(chatRoom.getGuest().getId())
						.boardId(chatRoom.getBoard().getId())
						.status(chatRoom.getStatus())
						.createdAt(chatRoom.getCreatedAt())
						.build();
	}

  private List<ChatRoomResponsePayload> buildChatRoomResponsePayload(List<ChatRoom> chatRooms, Long userId) {
  	return chatRooms.stream()
						.map(chatRoom -> {
							List<Message> messages = chatRoom.getMessages();
							boolean isHost = isSenderChatRoomHost(chatRoom.getId(), userId);
							long unreadMessages = countUnreadMessages(messages, isHost);

							Message lastMessage = null;
							if (messages.size() > 0) {
								lastMessage = messages.get(messages.size() - 1);
							}

							MessageResponsePayload messagePayload = null;
							if (lastMessage != null) {
								messagePayload = MessageResponsePayload.builder()
												.id(lastMessage.getId())
												.content(lastMessage.getContent())
												.type(lastMessage.getType())
												.isHostRead(lastMessage.isHostRead())
												.isGuestRead(lastMessage.isGuestRead())
												.senderId(lastMessage.getSender().getId())
												.senderNickname(lastMessage.getSender().getNickname())
												.likes(lastMessage.getProfileMessage().getLikes())
												.dislikes(lastMessage.getProfileMessage().getDislikes())
												.label(lastMessage.getProfileMessage().getLabel())
												.buttonLabel(lastMessage.getProfileMessage().getButtonLabel())
												.createdAt(lastMessage.getCreatedAt())
												.build();
							}

							return ChatRoomResponsePayload.builder()
											.id(chatRoom.getId())
											.hostId(chatRoom.getHost().getId())
											.guestId(chatRoom.getGuest().getId())
											.boardId(chatRoom.getBoard().getId())
											.status(chatRoom.getStatus())
											.lastMessage(messagePayload)
											.unreadMessages(unreadMessages)
											.createdAt(chatRoom.getCreatedAt())
											.build();
						})
						.collect(Collectors.toList());
	}

	private List<MessageResponsePayload> buildMessageResponsePayload(List<Message> messages, boolean isHost) {
  	return messages.stream()
						.map(message -> {
							message.readMessage(isHost);

							messageRepository.save(message);

							if (message.getProfileMessage() == null) {
								return MessageResponsePayload.builder()
												.id(message.getId())
												.content(message.getContent())
												.type(message.getType())
												.isHostRead(message.isHostRead())
												.isGuestRead(message.isGuestRead())
												.senderId(message.getSender().getId())
												.senderNickname(message.getSender().getNickname())
												.createdAt(message.getCreatedAt())
												.build();
							}
							return MessageResponsePayload.builder()
											.id(message.getId())
											.content(message.getContent())
											.type(message.getType())
											.isHostRead(message.isHostRead())
											.isGuestRead(message.isGuestRead())
											.senderId(message.getSender().getId())
											.senderNickname(message.getSender().getNickname())
											.likes(message.getProfileMessage().getLikes())
											.dislikes(message.getProfileMessage().getDislikes())
											.label(message.getProfileMessage().getLabel())
											.buttonLabel(message.getProfileMessage().getButtonLabel())
											.createdAt(message.getCreatedAt())
											.build();
						})
						.collect(Collectors.toList());
	}
}
