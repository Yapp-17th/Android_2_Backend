package com.yapp.crew.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.MessageResponsePayload;
import com.yapp.crew.service.ChattingConsumerService;
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

  private final ChattingConsumerService chattingConsumerService;

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final ChatRoomRepository chatRoomRepository;

  private final UserRepository userRepository;

  private final ObjectMapper objectMapper;

  @Autowired
	public ChattingConsumer(
					ChattingConsumerService chattingConsumerService,
					SimpMessagingTemplate simpMessagingTemplate,
					ChatRoomRepository chatRoomRepository,
					UserRepository userRepository,
					ObjectMapper objectMapper
	) {
  	this.chattingConsumerService = chattingConsumerService;
  	this.simpMessagingTemplate = simpMessagingTemplate;
  	this.chatRoomRepository = chatRoomRepository;
  	this.userRepository = userRepository;
  	this.objectMapper = objectMapper;
	}

  @Transactional
  @KafkaListener(topics = "${kafka.topics.chat-message}", groupId = "${kafka.groups.chat-message-group}")
  public void consumeChatMessage(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
    log.info("Consumer Record: {}", consumerRecord);

    MessageRequestPayload messageRequestPayload = objectMapper.readValue(consumerRecord.value(), MessageRequestPayload.class);

    ChatRoom chatRoom = chatRoomRepository.findById(messageRequestPayload.getChatRoomId())
            .orElseThrow(() -> new RuntimeException("Chat room not found"));

    User sender = userRepository.findById(messageRequestPayload.getSenderId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Message message = Message.getBuilder()
            .withContent(messageRequestPayload.getContent())
            .withType(messageRequestPayload.getType())
						.withIsRead(false)
            .withSender(sender)
            .withChatRoom(chatRoom)
            .build();

    chatRoom.addMessage(message);

    chattingConsumerService.processMessage(message);  // TODO: 바로 리포지토리에 저장하기

    MessageResponsePayload payload = buildMessageResponsePayload(message);
		simpMessagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoom().getId().toString(), payload);

		String messageJson = objectMapper.writeValueAsString(message);
    log.info("Successfully consumed message: {}", messageJson);
  }

  private MessageResponsePayload buildMessageResponsePayload(Message message) {
  	return MessageResponsePayload.builder()
						.id(message.getId())
						.content(message.getContent())
						.type(message.getType())
						.isRead(message.isRead())
						.senderId(message.getSender().getId())
						.senderNickname(message.getSender().getNickname())
						.createdAt(message.getCreatedAt())
						.build();
	}
}
