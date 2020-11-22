package com.yapp.crew.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.errors.AlreadyApprovedException;
import com.yapp.crew.domain.errors.AlreadyExitedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.CannotApplyException;
import com.yapp.crew.domain.errors.CannotApproveException;
import com.yapp.crew.domain.errors.CannotDisapproveException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.GuestApplyNotFoundException;
import com.yapp.crew.domain.errors.IsNotApprovedException;
import com.yapp.crew.domain.errors.MessageNotFoundException;
import com.yapp.crew.domain.errors.NoSpaceToApplyException;
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
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.network.HttpResponseBody;
import com.yapp.crew.payload.ApplyRequestPayload;
import com.yapp.crew.payload.ApproveRequestPayload;
import com.yapp.crew.payload.ChatRoomRequestPayload;
import com.yapp.crew.payload.ChatRoomResponsePayload;
import com.yapp.crew.payload.GuidelineRequestPayload;
import com.yapp.crew.payload.MessageResponsePayload;
import com.yapp.crew.payload.UserExitedPayload;
import com.yapp.crew.producer.ChattingProducer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ChattingService {

	private final ChattingProducer chattingProducer;

	private final AppliedUserRepository appliedUserRepository;

	private final ChatRoomRepository chatRoomRepository;

	private final MessageRepository messageRepository;

	private final BoardRepository boardRepository;

	private final UserRepository userRepository;

	@Autowired
	public ChattingService(
			ChattingProducer chattingProducer,
			AppliedUserRepository appliedUserRepository,
			ChatRoomRepository chatRoomRepository,
			MessageRepository messageRepository,
			BoardRepository boardRepository,
			UserRepository userRepository
	) {
		this.chattingProducer = chattingProducer;
		this.appliedUserRepository = appliedUserRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.messageRepository = messageRepository;
		this.boardRepository = boardRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public HttpResponseBody<ChatRoomResponsePayload> createChatRoom(ChatRoomRequestPayload chatRoomRequestPayload) throws JsonProcessingException {
		User host = userRepository.findUserById(chatRoomRequestPayload.getHostId())
				.orElseThrow(() -> new UserNotFoundException("Cannot find host user with id"));

		User guest = userRepository.findUserById(chatRoomRequestPayload.getGuestId())
				.orElseThrow(() -> new UserNotFoundException("Cannot find guest user"));

		User bot = userRepository.findUserById(-1L)
				.orElseThrow(() -> new UserNotFoundException("Cannot find chat bot"));

		Board board = boardRepository.findById(chatRoomRequestPayload.getBoardId())
				.orElseThrow(() -> new BoardNotFoundException("Cannot find board"));

		Optional<ChatRoom> chatRoom = chatRoomRepository.findByGuestIdAndBoardId(guest.getId(), board.getId());

		if (chatRoom.isPresent() && !chatRoom.get().getGuestExited() && !chatRoom.get().getHostExited()) {
			return HttpResponseBody.buildChatRoomResponse(
					ChatRoomResponsePayload.buildChatRoomResponsePayload(chatRoom.get()),
					HttpStatus.OK.value(),
					ResponseType.CHATROOM_ALREADY_CREATED
			);
		}

		ChatRoom newChatRoom = ChatRoom.buildChatRoom(host, guest, board);

		host.addChatRoomHost(newChatRoom);
		guest.addChatRoomGuest(newChatRoom);
		chatRoomRepository.save(newChatRoom);

		Optional<AppliedUser> appliedUser = appliedUserRepository.findByBoardIdAndUserId(board.getId(), guest.getId());
		if (appliedUser.isEmpty()) {
			AppliedUser newAppliedUser = AppliedUser.buildAppliedUser(guest, board, AppliedStatus.PENDING);

			guest.addAppliedUser(newAppliedUser);
			board.addAppliedUser(newAppliedUser);
			appliedUserRepository.save(newAppliedUser);
		}

		GuidelineRequestPayload guidelineRequestPayload = GuidelineRequestPayload.builder()
				.senderId(bot.getId())
				.chatRoomId(newChatRoom.getId())
				.build();

		chattingProducer.sendGuidelineBotMessage(guidelineRequestPayload);

		return HttpResponseBody.buildChatRoomResponse(
				ChatRoomResponsePayload.buildChatRoomResponsePayload(newChatRoom),
				HttpStatus.CREATED.value(),
				ResponseType.SUCCESS
		);
	}

	public HttpResponseBody<?> exitChatRoom(Long userId, Long chatRoomId) throws JsonProcessingException {
		User user = userRepository.findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

		boolean isHost = chatRoom.isUserChatRoomHost(userId);
		if (isHost && chatRoom.getHostExited()) {
			throw new AlreadyExitedException("This user already exited this chat room");
		}
		if (!isHost && chatRoom.getGuestExited()) {
			throw new AlreadyExitedException("This user already exited this chat room");
		}
		chatRoom.exitUser(isHost);
		chatRoomRepository.save(chatRoom);

		UserExitedPayload userExitedPayload = UserExitedPayload.builder()
				.chatRoomId(chatRoom.getId())
				.userId(user.getId())
				.build();

		chattingProducer.sendUserExitMessage(userExitedPayload);

		return HttpResponseBody.buildSuccessResponse(
				HttpStatus.OK.value(),
				ResponseType.SUCCESS,
				ResponseType.SUCCESS.getMessage()
		);
	}

	public HttpResponseBody<List<ChatRoomResponsePayload>> receiveChatRooms(Long userId) {
		List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId).stream()
				.filter(chatRoom -> {
					boolean isHost = chatRoom.isUserChatRoomHost(userId);
					if (isHost && chatRoom.getHostExited()) {
						return false;
					}
					if (!isHost && chatRoom.getGuestExited()) {
						return false;
					}
					return true;
				})
				.collect(Collectors.toList());

		return HttpResponseBody.buildChatRoomsResponse(
				ChatRoomResponsePayload.buildChatRoomResponsePayload(chatRooms, userId),
				HttpStatus.OK.value(),
				ResponseType.SUCCESS
		);
	}

	public HttpResponseBody<List<MessageResponsePayload>> receiveChatMessages(Long chatRoomId, Long userId) {
		List<Message> messages = messageRepository.findAllByChatRoomIdOrderByCreatedAt(chatRoomId);

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

		boolean isHost = chatRoom.isUserChatRoomHost(userId);
		Long firstUnreadChatMessageId = chatRoom.findFirstUnreadChatMessage(isHost);

		String boardTitle = chatRoom.getBoard().getTitle();

		AppliedUser appliedUser = appliedUserRepository.findByBoardIdAndUserId(chatRoom.getBoard().getId(), chatRoom.getGuest().getId())
				.orElseThrow(() -> new GuestApplyNotFoundException("Guest did not apply yet"));

		return HttpResponseBody.buildChatMessagesResponse(
				MessageResponsePayload.buildMessageResponsePayload(messageRepository, messages, isHost),
				HttpStatus.OK.value(),
				ResponseType.SUCCESS,
				firstUnreadChatMessageId,
				boardTitle,
				appliedUser.getStatus()
		);
	}

	@Transactional
	public HttpResponseBody<?> updateMessageIsRead(Long userId, Long chatRoomId, Long messageId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

		Message message = messageRepository.findById(messageId)
				.orElseThrow(() -> new MessageNotFoundException("Message not found"));

		boolean isHost = chatRoom.isUserChatRoomHost(userId);

		message.readMessage(isHost);
		messageRepository.save(message);

		return HttpResponseBody.buildSuccessResponse(
				HttpStatus.OK.value(),
				ResponseType.SUCCESS,
				ResponseType.SUCCESS.getMessage(),
				MessageResponsePayload.buildChatMessageResponsePayload(message)
		);
	}

	public HttpResponseBody<?> applyUser(ApplyRequestPayload applyRequestPayload) throws JsonProcessingException {
		ChatRoom chatRoom = chatRoomRepository.findById(applyRequestPayload.getChatRoomId())
				.orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

		if (chatRoom.getGuestExited() || chatRoom.getHostExited()) {
			throw new CannotApplyException("Cannot apply since user exited");
		}

		chattingProducer.applyUser(applyRequestPayload);

		return HttpResponseBody.buildSuccessResponse(
				HttpStatus.OK.value(),
				ResponseType.SUCCESS,
				ResponseType.SUCCESS.getMessage()
		);
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

		if (chatRoom.getGuestExited() || chatRoom.getHostExited()) {
			throw new CannotApproveException("Cannot approve since user exited");
		}

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

		if (board.getRemainRecruitNumber() < 1) {
			throw new NoSpaceToApplyException("This board is already full");
		}

		isApplied.approveUser();
		appliedUserRepository.save(isApplied);

		if (board.getRemainRecruitNumber() == 0) {
			board.completeRecruiting();
			boardRepository.save(board);
		}

		chattingProducer.approveUser(approveRequestPayload);

		return HttpResponseBody.buildSuccessResponse(
				HttpStatus.OK.value(),
				ResponseType.SUCCESS,
				ResponseType.SUCCESS.getMessage()
		);
	}

	public HttpResponseBody<?> disapproveUser(ApproveRequestPayload approveRequestPayload) throws JsonProcessingException {
		Board board = boardRepository.findById(approveRequestPayload.getBoardId())
				.orElseThrow(() -> new BoardNotFoundException("Board not found"));

		User host = userRepository.findUserById(approveRequestPayload.getHostId())
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		User guest = userRepository.findUserById(approveRequestPayload.getGuestId())
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		ChatRoom chatRoom = chatRoomRepository.findById(approveRequestPayload.getChatRoomId())
				.orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

		if (chatRoom.getGuestExited() || chatRoom.getHostExited()) {
			throw new CannotDisapproveException("Cannot disapprove since user exited");
		}

		if (!board.getUser().getId().equals(host.getId())) {
			throw new WrongHostException("This user is not a host for this board");
		}

		if (!chatRoom.getHost().getId().equals(host.getId())) {
			throw new WrongHostException("This user is not a host for this chat room");
		}

		if (!chatRoom.getGuest().getId().equals(guest.getId())) {
			throw new WrongGuestException("This user is not a guest for this chat room");
		}

		AppliedUser isApproved = appliedUserRepository.findByBoardIdAndUserId(board.getId(), guest.getId())
				.orElseThrow(() -> new GuestApplyNotFoundException("This user did not apply"));

		if (!isApproved.getStatus().equals(AppliedStatus.APPROVED)) {
			throw new IsNotApprovedException("This user is not approved");
		}

		isApproved.disapproveUser();
		appliedUserRepository.save(isApproved);

		chattingProducer.disapproveUser(approveRequestPayload);

		return HttpResponseBody.buildSuccessResponse(
				HttpStatus.OK.value(),
				ResponseType.SUCCESS,
				ResponseType.SUCCESS.getMessage()
		);
	}
}
