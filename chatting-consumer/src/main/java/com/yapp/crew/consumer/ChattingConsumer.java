package com.yapp.crew.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.payload.MessagePayload;
import com.yapp.crew.service.ChattingConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ChattingConsumer {

  @Autowired
  private ChattingConsumerService chattingConsumerService;

//  @Autowired
//  private SimpMessagingTemplate simpMessagingTemplate;

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

    MessagePayload messagePayload = objectMapper.readValue(consumerRecord.value(), MessagePayload.class);

    ChatRoom chatRoom = chatRoomRepository.findById(messagePayload.getChatRoomId())
            .orElseThrow(() -> new RuntimeException("Chat room not found"));

    User sender = userRepository.findById(messagePayload.getSenderId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Message message = Message.getBuilder()
            .withContent(messagePayload.getContent())
            .withType(messagePayload.getType())
            .withSender(sender)
            .withChatRoom(chatRoom)
            .build();

    chatRoom.addMessage(message);

    String messageJson = objectMapper.writeValueAsString(message);

    chattingConsumerService.processMessage(message);

//		simpMessagingTemplate.convertAndSend("/topic/" + message.getChatRoom().getId().toString(), messageJson);
    log.info("Successfully consumed message: {}", messageJson);
  }
}
