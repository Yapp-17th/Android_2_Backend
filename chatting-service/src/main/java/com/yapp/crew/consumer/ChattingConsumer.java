package com.yapp.crew.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.MessageRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.MessageResponsePayload;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "Chatting Consumer")
@Component
public class ChattingConsumer {

	private final SimpMessagingTemplate simpMessagingTemplate;

	private final ChatRoomRepository chatRoomRepository;

	private final MessageRepository messageRepository;

	private final UserRepository userRepository;

	private final ObjectMapper objectMapper;

	@Autowired
	public ChattingConsumer(
			SimpMessagingTemplate simpMessagingTemplate,
			ChatRoomRepository chatRoomRepository,
			MessageRepository messageRepository,
			UserRepository userRepository,
			ObjectMapper objectMapper
	) {
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.chatRoomRepository = chatRoomRepository;
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
		this.objectMapper = objectMapper;
	}

	@Transactional
	@KafkaListener(topics = "${kafka.topics.chat-message}", groupId = "${kafka.groups.chat-message-group}")
	public void consumeChatMessage(ConsumerRecord<Long, String> consumerRecord) throws JsonProcessingException {
		log.info("Consumer Record: {}", consumerRecord);

		MessageRequestPayload messageRequestPayload = objectMapper.readValue(consumerRecord.value(), MessageRequestPayload.class);

		ChatRoom chatRoom = chatRoomRepository.findById(messageRequestPayload.getChatRoomId())
				.orElseThrow(() -> new ChatRoomNotFoundException(messageRequestPayload.getChatRoomId()));

		User sender = userRepository.findById(messageRequestPayload.getSenderId())
				.orElseThrow(() -> new UserNotFoundException(messageRequestPayload.getSenderId()));

		long lastMessageId = 0;
		Optional<Message> lastMessage = messageRepository.findFirstByChatRoomIdOrderByIdDesc(chatRoom.getId());
		if (lastMessage.isPresent()) {
			lastMessageId = lastMessage.get().getMessageId() + 1;
		}

		Message message = Message.buildChatMessage(
				messageRequestPayload.getContent(),
				messageRequestPayload.getType(),
				lastMessageId,
				sender,
				chatRoom
		);
		chatRoom.addMessage(message);

		if (chatRoom.getGuestExited() && chatRoom.getHostExited()) {
			chatRoom.inactivateChatRoom();
		}
		chatRoomRepository.save(chatRoom);
		messageRepository.save(message);

		MessageResponsePayload payload = MessageResponsePayload.buildChatMessageResponsePayload(message);
		sendMessage(message, sender, chatRoom, payload);
	}

	private void sendMessage(Message message, User sender, ChatRoom chatRoom, MessageResponsePayload payload) {
		simpMessagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoom().getId().toString(), payload);

		if (sender.getId().equals(chatRoom.getGuest().getId())) {
			simpMessagingTemplate.convertAndSend("/sub/user/" + chatRoom.getHost().getId().toString() + "/chat/room", payload);
		}
		else if (sender.getId().equals(chatRoom.getHost().getId())) {
			simpMessagingTemplate.convertAndSend("/sub/user/" + chatRoom.getGuest().getId().toString() + "/chat/room", payload);
		}
		else {
			simpMessagingTemplate.convertAndSend("/sub/user/" + chatRoom.getHost().getId().toString() + "/chat/room", payload);
			simpMessagingTemplate.convertAndSend("/sub/user/" + chatRoom.getGuest().getId().toString() + "/chat/room", payload);
		}
	}
}
