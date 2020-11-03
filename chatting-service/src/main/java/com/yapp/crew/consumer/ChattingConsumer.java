package com.yapp.crew.consumer;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.AppliedUser;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.AppliedUserRepository;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.MessageRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.type.MessageType;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.MessageResponsePayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ChattingConsumer {

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final EvaluationRepository evaluationRepository;

  private final ChatRoomRepository chatRoomRepository;

  private final MessageRepository messageRepository;

  private final AppliedUserRepository appliedUserRepository;

  private final UserRepository userRepository;

  private final BoardRepository boardRepository;

  private final ObjectMapper objectMapper;

  @Autowired
	public ChattingConsumer(
					SimpMessagingTemplate simpMessagingTemplate,
					EvaluationRepository evaluationRepository,
					ChatRoomRepository chatRoomRepository,
					MessageRepository messageRepository,
					AppliedUserRepository appliedUserRepository,
					UserRepository userRepository,
					BoardRepository boardRepository,
					ObjectMapper objectMapper
	) {
  	this.simpMessagingTemplate = simpMessagingTemplate;
  	this.evaluationRepository = evaluationRepository;
  	this.chatRoomRepository = chatRoomRepository;
  	this.messageRepository = messageRepository;
  	this.appliedUserRepository = appliedUserRepository;
  	this.userRepository = userRepository;
  	this.boardRepository = boardRepository;
  	this.objectMapper = objectMapper;
	}

  @Transactional
  @KafkaListener(topics = "${kafka.topics.chat-message}", groupId = "${kafka.groups.chat-message-group}")
  public void consumeChatMessage(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
    log.info("Consumer Record: {}", consumerRecord);

    MessageRequestPayload messageRequestPayload = objectMapper.readValue(consumerRecord.value(), MessageRequestPayload.class);

    ChatRoom chatRoom = chatRoomRepository.findById(messageRequestPayload.getChatRoomId())
            .orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found"));

    User sender = userRepository.findById(messageRequestPayload.getSenderId())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    Message message;
		if (messageRequestPayload.getType().equals(MessageType.PROFILE)) {
			List<Evaluation> evaluations = evaluationRepository.findAllByUserId(sender.getId());

			message = Message.buildProfileMessage(messageRequestPayload.getContent(), sender, chatRoom, evaluations);

			Board board = boardRepository.findById(messageRequestPayload.getBoardId())
							.orElseThrow(() -> new BoardNotFoundException("Board not found"));

			AppliedUser appliedUser = AppliedUser.buildAppliedUser(sender, board, AppliedStatus.PENDING);
			appliedUserRepository.save(appliedUser);

			sender.addAppliedUser(appliedUser);
			board.addAppliedUser(appliedUser);
		}
		else {
			message = Message.buildChatMessage(messageRequestPayload.getContent(), messageRequestPayload.getType(), sender, chatRoom);
		}
    chatRoom.addMessage(message);
    messageRepository.save(message);

    MessageResponsePayload payload = buildMessageResponsePayload(message);
		simpMessagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoom().getId().toString(), payload);

		String messageJson = objectMapper.writeValueAsString(message);
    log.info("Successfully consumed message: {}", messageJson);
  }

  private MessageResponsePayload buildMessageResponsePayload(Message message) {
  	if (message.getProfileMessage() == null) {
			return MessageResponsePayload.buildChatMessageResponsePayload(message);
		}
  	return MessageResponsePayload.buildProfileMessageResponsePayload(message);
	}
}
