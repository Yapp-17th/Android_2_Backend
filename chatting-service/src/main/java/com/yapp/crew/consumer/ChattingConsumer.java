package com.yapp.crew.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.MessageSocketPayload;
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

  @Autowired
  private ChattingConsumerService chattingConsumerService;

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  private ChatRoomRepository chatRoomRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(topics = {"explanet-dev"}, groupId = "explanet-dev-group")
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

    chattingConsumerService.processMessage(message);

    MessageSocketPayload payload = buildMessageSocketPayload(message);
		simpMessagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoom().getId().toString(), payload);

		String messageJson = objectMapper.writeValueAsString(message);
    log.info("Successfully consumed message: {}", messageJson);
  }

  private MessageSocketPayload buildMessageSocketPayload(Message message) {
  	return MessageSocketPayload.builder()
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
