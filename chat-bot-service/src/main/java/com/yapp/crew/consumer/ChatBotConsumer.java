package com.yapp.crew.consumer;

import java.util.List;

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
import com.yapp.crew.payload.GuidelineRequestPayload;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.producer.ChatBotProducer;
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
				.type(MessageType.BOT_MESSAGE)
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
				.content(String.format(botMessages.getApplyMessage(), applier.getNickname()).replace("\"", ""))
				.type(MessageType.BOT_MESSAGE)
				.senderId(bot.getId())
				.chatRoomId(chatRoom.getId())
				.boardId(board.getId())
				.build();

		List<Evaluation> evaluations = evaluationRepository.findAllByUserId(applier.getId());

		if (appliedUserRepository.findByBoardIdAndUserId(board.getId(), applier.getId()).isPresent()) {
			throw new AlreadyAppliedException("Already applied");
		}

		AppliedUser newApply = AppliedUser.buildAppliedUser(applier, board, AppliedStatus.PENDING);
		appliedUserRepository.save(newApply);

		applier.addAppliedUser(newApply);
		board.addAppliedUser(newApply);

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
	public void consumeBotEventAcceptUser(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - Approve User] Consumer Record: {}", consumerRecord);

		ApproveRequestPayload approveRequestPayload = objectMapper.readValue(consumerRecord.value(), ApproveRequestPayload.class);

		User host = userRepository.findUserById(approveRequestPayload.getHostId())
				.orElseThrow(() -> new UserNotFoundException("[Not Found] User not found"));

		User bot = userRepository.findUserById(-1L)
				.orElseThrow(() -> new UserNotFoundException("[Not Found] Bot not found"));

		MessageRequestPayload approveMessagePayload = MessageRequestPayload.builder()
				.content(String.format(botMessages.getApproveMessage(), host.getNickname()).replace("\"", ""))
				.type(MessageType.BOT_MESSAGE)
				.senderId(bot.getId())
				.chatRoomId(approveRequestPayload.getChatRoomId())
				.boardId(approveRequestPayload.getBoardId())
				.build();

		chatBotProducer.sendBotMessage(approveMessagePayload);
	}
}
