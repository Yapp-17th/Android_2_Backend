package com.yapp.crew.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatBotConsumer {

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = {"request-user-profile"}, groupId = "request-user-profile-group")
	public void consumeBotEventRequestUserProfile(ConsumerRecord<Long, String> consumerRecord) {
		log.info("[Chat Bot Event - Request User Profile] Consumer Record: {}", consumerRecord);

	}

	@KafkaListener(topics = {"accept-user"}, groupId = "accept-user-group")
	public void consumeBotEventAcceptUser(ConsumerRecord<Long, String> consumerRecord) {
		log.info("[Chat Bot Event - Accept User] Consumer Record: {}", consumerRecord);


	}
}
