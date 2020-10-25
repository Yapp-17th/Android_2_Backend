package com.yapp.crew.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.producer.ChatBotProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatBotConsumer {

	private final ChatBotProducer chatBotProducer;

	private final ChatRoomRepository chatRoomRepository;

	private final UserRepository userRepository;

	private final ObjectMapper objectMapper;

	@Autowired
	public ChatBotConsumer(
					ChatBotProducer chatBotProducer,
					ChatRoomRepository chatRoomRepository,
					UserRepository userRepository,
					ObjectMapper objectMapper
	) {
		this.chatBotProducer = chatBotProducer;
		this.chatRoomRepository = chatRoomRepository;
		this.userRepository = userRepository;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = {"welcome-message"}, groupId = "welcome-message-group")
	public void consumeBotWelcomeMessage(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("[Chat Bot Event - Welcome Message] Consumer Record: {}", consumerRecord);

		MessageRequestPayload messageRequestPayload = objectMapper.readValue(consumerRecord.value(), MessageRequestPayload.class);

		chatBotProducer.sendBotMessage(messageRequestPayload);
	}

	@KafkaListener(topics = {"request-user-profile"}, groupId = "request-user-profile-group")
	public void consumeBotEventRequestUserProfile(ConsumerRecord<Long, String> consumerRecord) {
		log.info("[Chat Bot Event - Request User Profile] Consumer Record: {}", consumerRecord);

	}

	@KafkaListener(topics = {"accept-user"}, groupId = "accept-user-group")
	public void consumeBotEventAcceptUser(ConsumerRecord<Long, String> consumerRecord) {
		log.info("[Chat Bot Event - Accept User] Consumer Record: {}", consumerRecord);

	}
}
