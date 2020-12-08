package com.yapp.crew.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.yapp.crew.domain.errors.AlreadyApprovedException;
import com.yapp.crew.domain.errors.AlreadyExitedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.CannotApplyException;
import com.yapp.crew.domain.errors.CannotApplyToMyBoardException;
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
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.network.HttpResponseBody;
import com.yapp.crew.payload.ApplyRequestPayload;
import com.yapp.crew.payload.ApproveRequestPayload;
import com.yapp.crew.payload.ChatRoomRequestPayload;
import com.yapp.crew.payload.ChatRoomResponsePayload;
import com.yapp.crew.payload.GuidelineRequestPayload;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.MessageResponsePayload;
import com.yapp.crew.payload.UserExitedPayload;
import com.yapp.crew.producer.ChattingProducer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
				.orElseThrow(() -> new UserNotFoundException(chatRoomRequestPayload.getHostId()));

		User guest = userRepository.findUserById(chatRoomRequestPayload.getGuestId())
				.orElseThrow(() -> new UserNotFoundException(chatRoomRequestPayload.getGuestId()));

		User bot = userRepository.findUserById(-9L)
				.orElseThrow(() -> new UserNotFoundException(-9L));

		Board board = boardRepository.findById(chatRoomRequestPayload.getBoardId())
				.orElseThrow(() -> new BoardNotFoundException(chatRoomRequestPayload.getBoardId()));

		if (board.getUser().getId().equals(guest.getId())) {
			throw new CannotApplyToMyBoardException(guest.getId(), board.getId());
		}

		Optional<ChatRoom> chatRoom = chatRoomRepository.findByGuestIdAndBoardId(guest.getId(), board.getId());

		if (chatRoom.isPresent() && !chatRoom.get().getGuestExited() && !chatRoom.get().getHostExited()) {
			return HttpResponseBody.buildChatRoomResponse(
					ChatRoomResponsePayload.buildChatRoomResponsePayload(chatRoom.get(), host),
					HttpStatus.OK.value(),
					ResponseType.CHATROOM_ALREADY_CREATED
			);
		}

		else if (chatRoom.isPresent() && chatRoom.get().getGuestExited() && !chatRoom.get().getHostExited()) {
			chatRoom.get().inviteUser(false);
			chatRoomRepository.save(chatRoom.get());
			return HttpResponseBody.buildChatRoomResponse(
					ChatRoomResponsePayload.buildChatRoomResponsePayload(chatRoom.get(), host),
					HttpStatus.OK.value(),
					ResponseType.SUCCESS
			);
		}

		else if (chatRoom.isPresent() && !chatRoom.get().getGuestExited() && chatRoom.get().getHostExited()) {
			chatRoom.get().inviteUser(true);
			chatRoomRepository.save(chatRoom.get());
			return HttpResponseBody.buildChatRoomResponse(
					ChatRoomResponsePayload.buildChatRoomResponsePayload(chatRoom.get(), host),
					HttpStatus.OK.value(),
					ResponseType.SUCCESS
			);
		}

		else {
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
					ChatRoomResponsePayload.buildChatRoomResponsePayload(newChatRoom, host),
					HttpStatus.CREATED.value(),
					ResponseType.SUCCESS
			);
		}
	}

	public HttpResponseBody<?> exitChatRoom(Long userId, Long chatRoomId) throws JsonProcessingException {
		User user = userRepository.findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId));

		boolean isHost = chatRoom.isUserChatRoomHost(userId);
		if (isHost && chatRoom.getHostExited()) {
			throw new AlreadyExitedException(userId, chatRoomId);
		}
		if (!isHost && chatRoom.getGuestExited()) {
			throw new AlreadyExitedException(userId, chatRoomId);
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

	@Transactional
	public HttpResponseBody<List<MessageResponsePayload>> receiveChatMessages(Long chatRoomId, Long userId) {
		List<Message> messages = messageRepository.findAllByChatRoomIdOrderByCreatedAt(chatRoomId);

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId));

		boolean isHost = chatRoom.isUserChatRoomHost(userId);
		Long firstUnreadChatMessageId = chatRoom.findFirstUnreadChatMessage(isHost);
		if (firstUnreadChatMessageId != -1L) {
			List<Message> unreadMessages = messageRepository.findAllByChatRoomIdAndMessageIdGreaterThan(chatRoomId, firstUnreadChatMessageId - 1);
			unreadMessages.forEach(message -> message.readMessage(isHost));
			messageRepository.saveAll(unreadMessages);
		}

		String boardTitle = chatRoom.getBoard().getTitle();

		AppliedUser appliedUser = appliedUserRepository.findByBoardIdAndUserId(chatRoom.getBoard().getId(), chatRoom.getGuest().getId())
				.orElseThrow(() -> new GuestApplyNotFoundException(chatRoom.getGuest().getId(), chatRoom.getBoard().getId()));

		return HttpResponseBody.buildChatMessagesResponse(
				MessageResponsePayload.buildMessageResponsePayload(messageRepository, messages, isHost),
				HttpStatus.OK.value(),
				ResponseType.SUCCESS,
				firstUnreadChatMessageId,
				boardTitle,
				appliedUser.getStatus(),
				chatRoom
		);
	}

	@Transactional
	public HttpResponseBody<?> messageUpdate(Long userId, Long chatRoomId, Long messageId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId));

		Message message = messageRepository.findById(messageId)
				.orElseThrow(() -> new MessageNotFoundException(messageId));

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

	public void messageBulkUpdate(MessageRequestPayload payload) {
		String messagesRawString = CharMatcher.anyOf("[]").replaceFrom(payload.getContent(), "");
		if (!messagesRawString.equals("")) {
			List<String> messageStringList = Splitter.on(',')
					.trimResults()
					.omitEmptyStrings()
					.splitToList(messagesRawString);
			List<Long> messageIdList = Lists.transform(messageStringList, Long::parseLong);

			ChatRoom chatRoom = chatRoomRepository.findById(payload.getChatRoomId())
					.orElseThrow(() -> new ChatRoomNotFoundException(payload.getChatRoomId()));

			boolean isHost = chatRoom.isUserChatRoomHost(payload.getSenderId());

			List<Message> messages = messageRepository.findAllByIdIn(messageIdList);
			messages.forEach(message -> message.readMessage(isHost));
			messageRepository.saveAll(messages);
		}
	}

	public HttpResponseBody<?> applyUser(ApplyRequestPayload applyRequestPayload) throws JsonProcessingException {
		ChatRoom chatRoom = chatRoomRepository.findById(applyRequestPayload.getChatRoomId())
				.orElseThrow(() -> new ChatRoomNotFoundException(applyRequestPayload.getChatRoomId()));

		Board board = boardRepository.findById(applyRequestPayload.getBoardId())
				.orElseThrow(() -> new BoardNotFoundException(applyRequestPayload.getBoardId()));

		if (board.getStatus() == BoardStatus.CANCELED || board.getStatus() == BoardStatus.FINISHED) {
			throw new CannotApplyException(applyRequestPayload.getBoardId());
		}

		if (chatRoom.getGuestExited() || chatRoom.getHostExited()) {
			throw new CannotApplyException(applyRequestPayload.getBoardId());
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
				.orElseThrow(() -> new BoardNotFoundException(approveRequestPayload.getBoardId()));

		User host = userRepository.findUserById(approveRequestPayload.getHostId())
				.orElseThrow(() -> new UserNotFoundException(approveRequestPayload.getHostId()));

		User guest = userRepository.findUserById(approveRequestPayload.getGuestId())
				.orElseThrow(() -> new UserNotFoundException(approveRequestPayload.getGuestId()));

		ChatRoom chatRoom = chatRoomRepository.findById(approveRequestPayload.getChatRoomId())
				.orElseThrow(() -> new ChatRoomNotFoundException(approveRequestPayload.getChatRoomId()));

		if (board.getStatus() == BoardStatus.CANCELED || board.getStatus() == BoardStatus.FINISHED) {
			throw new CannotApproveException(approveRequestPayload.getBoardId());
		}

		if (chatRoom.getGuestExited() || chatRoom.getHostExited()) {
			throw new CannotApproveException(approveRequestPayload.getBoardId());
		}

		if (!board.getUser().getId().equals(host.getId())) {
			throw new WrongHostException(host.getId(), board.getId(), "board");
		}

		if (!chatRoom.getHost().getId().equals(host.getId())) {
			throw new WrongHostException(host.getId(), chatRoom.getId(), "chat room");
		}

		if (!chatRoom.getGuest().getId().equals(guest.getId())) {
			throw new WrongGuestException(guest.getId(), chatRoom.getId());
		}

		AppliedUser isApplied = appliedUserRepository.findByBoardIdAndUserId(board.getId(), guest.getId())
				.orElseThrow(() -> new GuestApplyNotFoundException(guest.getId(), board.getId()));

		if (isApplied.getStatus().equals(AppliedStatus.APPROVED)) {
			throw new AlreadyApprovedException(guest.getId(), board.getId());
		}

		if (board.getRemainRecruitNumber() < 1) {
			throw new NoSpaceToApplyException(board.getId());
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
				.orElseThrow(() -> new BoardNotFoundException(approveRequestPayload.getBoardId()));

		User host = userRepository.findUserById(approveRequestPayload.getHostId())
				.orElseThrow(() -> new UserNotFoundException(approveRequestPayload.getHostId()));

		User guest = userRepository.findUserById(approveRequestPayload.getGuestId())
				.orElseThrow(() -> new UserNotFoundException(approveRequestPayload.getGuestId()));

		ChatRoom chatRoom = chatRoomRepository.findById(approveRequestPayload.getChatRoomId())
				.orElseThrow(() -> new ChatRoomNotFoundException(approveRequestPayload.getChatRoomId()));

		if (board.getStatus() == BoardStatus.CANCELED || board.getStatus() == BoardStatus.FINISHED) {
			throw new CannotDisapproveException(board.getId());
		}

		if (chatRoom.getGuestExited() || chatRoom.getHostExited()) {
			throw new CannotDisapproveException(board.getId());
		}

		if (!board.getUser().getId().equals(host.getId())) {
			throw new WrongHostException(host.getId(), board.getId(), "board");
		}

		if (!chatRoom.getHost().getId().equals(host.getId())) {
			throw new WrongHostException(host.getId(), chatRoom.getId(), "chat room");
		}

		if (!chatRoom.getGuest().getId().equals(guest.getId())) {
			throw new WrongGuestException(guest.getId(), chatRoom.getId());
		}

		AppliedUser isApproved = appliedUserRepository.findByBoardIdAndUserId(board.getId(), guest.getId())
				.orElseThrow(() -> new GuestApplyNotFoundException(guest.getId(), board.getId()));

		if (!isApproved.getStatus().equals(AppliedStatus.APPROVED)) {
			throw new IsNotApprovedException(guest.getId(), board.getId());
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
