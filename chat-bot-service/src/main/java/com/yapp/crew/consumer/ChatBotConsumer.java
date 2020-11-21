package com.yapp.crew.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.domain.errors.AlreadyAppliedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.AppliedUser;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.AppliedUserRepository;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.type.MessageType;
import com.yapp.crew.json.BotMessages;
import com.yapp.crew.payload.ApplyRequestPayload;
import com.yapp.crew.payload.ApproveRequestPayload;
import com.yapp.crew.payload.BoardCanceledPayload;
import com.yapp.crew.payload.BoardFinishedPayload;
import com.yapp.crew.payload.GuidelineRequestPayload;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.UserExitedPayload;
import com.yapp.crew.producer.ChatBotProducer;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ChatBotConsumer {

	private final BotMessages botMessages;

	private final ChatBotProducer chatBotProducer;

	private final AppliedUserRepository appliedUserRepository;

	private final EvaluationRepository evaluationRepository;

	private final ChatRoomRepository chatRoomRepository;

	private final BoardRepository boardRepository;

	private final UserRepository userRepository;

	private final ObjectMapper objectMapper;

	@Autowired
	public ChatBotConsumer(
			BotMessages botMessages,
			ChatBotProducer chatBotProducer,
			AppliedUserRepository appliedUserRepository,
			EvaluationRepository evaluationRepository,
			ChatRoomRepository chatRoomRepository,
			BoardRepository boardRepository,
			UserRepository userRepository,
			ObjectMapper objectMapper
	) {
		this.botMessages = botMessages;
		this.chatBotProducer = chatBotProducer;
		this.appliedUserRepository = appliedUserRepository;
		this.evaluationRepository = evaluationRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.boardRepository = boardRepository;
		this.userRepository = userRepository;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = "${kafka.topics.guideline-message}", groupId = "${kafka.groups.guideline-message-group}")
	public void consumeBotGuidelineMessage(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - Guideline Message] Consumer Record: {}", consumerRecord);

		GuidelineRequestPayload guidelineRequestPayload = objectMapper.readValue(consumerRecord.value(), GuidelineRequestPayload.class);

		MessageRequestPayload guidelineMessagePayload = MessageRequestPayload.builder()
				.content(botMessages.getGuidelineMessage().replace("\"", ""))
				.type(MessageType.BOT_NOTICE)
				.senderId(guidelineRequestPayload.getSenderId())
				.chatRoomId(guidelineRequestPayload.getChatRoomId())
				.build();

		chatBotProducer.sendBotMessage(guidelineMessagePayload);
	}

	@Transactional
	@KafkaListener(topics = "${kafka.topics.apply-user}", groupId = "${kafka.groups.apply-user-group}")
	public void consumeBotEventApplyUser(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - Apply User] Consumer Record: {}", consumerRecord);

		ApplyRequestPayload applyRequestPayload = objectMapper.readValue(consumerRecord.value(), ApplyRequestPayload.class);

		User applier = userRepository.findUserById(applyRequestPayload.getApplierId())
				.orElseThrow(() -> new UserNotFoundException("[Not Found] User not found"));

		User bot = userRepository.findUserById(-1L)
				.orElseThrow(() -> new UserNotFoundException("[Not Found] Bot not found"));

		Board board = boardRepository.findById(applyRequestPayload.getBoardId())
				.orElseThrow(() -> new BoardNotFoundException("[Not Found] Board not found"));

		ChatRoom chatRoom = chatRoomRepository.findById(applyRequestPayload.getChatRoomId())
				.orElseThrow(() -> new ChatRoomNotFoundException("[Not Found] Chat room not found"));

		MessageRequestPayload applyMessagePayload = MessageRequestPayload.builder()
				.content(String.format(
						botMessages.getApplyMessage(),
						applier.getNickname()
				).replace("\"", ""))
				.type(MessageType.BOT_MESSAGE)
				.senderId(bot.getId())
				.chatRoomId(chatRoom.getId())
				.boardId(board.getId())
				.build();

		List<Evaluation> evaluations = evaluationRepository.findAllByEvaluatedId(applier.getId());

		Optional<AppliedUser> appliedUser = appliedUserRepository.findByBoardIdAndUserId(board.getId(), applier.getId());
		if (appliedUser.isPresent() && (appliedUser.get().getStatus().equals(AppliedStatus.APPLIED) || appliedUser.get().getStatus()
				.equals(AppliedStatus.APPROVED))) {
			throw new AlreadyAppliedException("Already applied");
		}
		appliedUser.get().applyUser();
		appliedUserRepository.save(appliedUser.get());

		MessageRequestPayload profileMessagePayload = MessageRequestPayload.builder()
				.content(String.format(
						botMessages.getProfileMessage(),
						applier.getNickname(),
						applier.calculateLikes(evaluations),
						applier.calculateDislikes(evaluations),
						applier.getIntro()
				).replace("\"", ""))
				.type(MessageType.PROFILE)
				.senderId(applier.getId())
				.chatRoomId(chatRoom.getId())
				.boardId(board.getId())
				.build();

		chatBotProducer.sendBotMessage(applyMessagePayload);
		chatBotProducer.sendBotMessage(profileMessagePayload);
	}

	@KafkaListener(topics = "${kafka.topics.approve-user}", groupId = "${kafka.groups.approve-user-group}")
	public void consumeBotEventApproveUser(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - Approve User] Consumer Record: {}", consumerRecord);

		ApproveRequestPayload approveRequestPayload = objectMapper.readValue(consumerRecord.value(), ApproveRequestPayload.class);

		User host = userRepository.findUserById(approveRequestPayload.getHostId())
				.orElseThrow(() -> new UserNotFoundException("[Not Found] User not found"));

		User bot = userRepository.findUserById(-1L)
				.orElseThrow(() -> new UserNotFoundException("[Not Found] Bot not found"));

		MessageRequestPayload approveMessagePayload = MessageRequestPayload.builder()
				.content(String.format(
						botMessages.getApproveMessage(),
						host.getNickname()
				).replace("\"", ""))
				.type(MessageType.BOT_MESSAGE)
				.senderId(bot.getId())
				.chatRoomId(approveRequestPayload.getChatRoomId())
				.boardId(approveRequestPayload.getBoardId())
				.build();

		chatBotProducer.sendBotMessage(approveMessagePayload);
	}

	@KafkaListener(topics = "${kafka.topics.disapprove-user}", groupId = "${kafka.groups.disapprove-user-group}")
	public void consumeBotEventDisapproveUser(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - Disapprove User] Consumer Record: {}", consumerRecord);

		ApproveRequestPayload approveRequestPayload = objectMapper.readValue(consumerRecord.value(), ApproveRequestPayload.class);

		User host = userRepository.findUserById(approveRequestPayload.getHostId())
				.orElseThrow(() -> new UserNotFoundException("[Not Found] User not found"));

		User bot = userRepository.findUserById(-1L)
				.orElseThrow(() -> new UserNotFoundException("[Not Found] Bot not found"));

		MessageRequestPayload disapproveMessagePayload = MessageRequestPayload.builder()
				.content(String.format(
						botMessages.getDisapproveMessage(),
						host.getNickname()
				).replace("\"", ""))
				.type(MessageType.BOT_MESSAGE)
				.senderId(bot.getId())
				.chatRoomId(approveRequestPayload.getChatRoomId())
				.boardId(approveRequestPayload.getBoardId())
				.build();

		chatBotProducer.sendBotMessage(disapproveMessagePayload);
	}

	@KafkaListener(topics = "${kafka.topics.board-finished}", groupId = "${kafka.groups.board-finished-group}")
	public void consumeBoardFinishedEvent(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - Board Finished] Consumer Record: {}", consumerRecord);

		BoardFinishedPayload boardFinishedPayload = objectMapper.readValue(consumerRecord.value(), BoardFinishedPayload.class);

		Board board = boardRepository.findById(boardFinishedPayload.getBoardId())
				.orElseThrow(() -> new BoardNotFoundException("Board not found"));

		User bot = userRepository.findUserById(-1L)
				.orElseThrow(() -> new UserNotFoundException("Bot not found"));

		chatRoomRepository.findAllByBoardId(board.getId())
				.forEach(chatRoom -> {
					MessageRequestPayload boardFinishedMessagePayload = MessageRequestPayload.builder()
							.content(botMessages.getBoardFinished().replace("\"", ""))
							.type(MessageType.BOT_MESSAGE)
							.senderId(bot.getId())
							.chatRoomId(chatRoom.getId())
							.boardId(board.getId())
							.build();

					try {
						chatBotProducer.sendBotMessage(boardFinishedMessagePayload);
					} catch (JsonProcessingException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				});
	}

	@KafkaListener(topics = "${kafka.topics.board-canceled}", groupId = "${kafka.groups.board-canceled-group}")
	public void consumeBoardCanceledEvent(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - Board Canceled] Consumer Record: {}", consumerRecord);

		BoardCanceledPayload boardCanceledPayload = objectMapper.readValue(consumerRecord.value(), BoardCanceledPayload.class);

		Board board = boardRepository.findById(boardCanceledPayload.getBoardId())
				.orElseThrow(() -> new BoardNotFoundException("Board not found"));

		User host = userRepository.findUserById(boardCanceledPayload.getUserId())
				.orElseThrow(() -> new UserNotFoundException("Host not found"));

		User bot = userRepository.findUserById(-1L)
				.orElseThrow(() -> new UserNotFoundException("Bot not found"));

		chatRoomRepository.findAllByBoardId(board.getId())
				.forEach(chatRoom -> {
					MessageRequestPayload boardCanceledMessagePayload = MessageRequestPayload.builder()
							.content(String.format(
									botMessages.getBoardCanceled(),
									host.getNickname()
							).replace("\"", ""))
							.type(MessageType.BOT_MESSAGE)
							.senderId(bot.getId())
							.chatRoomId(chatRoom.getId())
							.boardId(board.getId())
							.build();

					try {
						chatBotProducer.sendBotMessage(boardCanceledMessagePayload);
					} catch (JsonProcessingException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				});
	}

	@KafkaListener(topics = "${kafka.topics.user-exited}", groupId = "${kafka.groups.user-exited-group}")
	public void consumeUserExitedEvent(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - User Exited] Consumer Record: {}", consumerRecord);

		UserExitedPayload userExitedPayload = objectMapper.readValue(consumerRecord.value(), UserExitedPayload.class);

		ChatRoom chatRoom = chatRoomRepository.findById(userExitedPayload.getChatRoomId())
				.orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

		User user = userRepository.findUserById(userExitedPayload.getUserId())
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		User bot = userRepository.findUserById(-1L)
				.orElseThrow(() -> new UserNotFoundException("Bot not found"));

		MessageRequestPayload userExitedMessagePayload = MessageRequestPayload.builder()
				.content(String.format(
						botMessages.getUserExited(),
						user.getNickname()
				).replace("\"", ""))
				.type(MessageType.BOT_MESSAGE)
				.senderId(bot.getId())
				.chatRoomId(chatRoom.getId())
				.build();

		chatBotProducer.sendBotMessage(userExitedMessagePayload);
	}
}
