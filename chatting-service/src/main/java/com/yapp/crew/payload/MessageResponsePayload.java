package com.yapp.crew.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.repository.MessageRepository;
import com.yapp.crew.domain.type.MessageType;
import com.yapp.crew.domain.type.RealTimeUpdateType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponsePayload {

	private long id;

	private String content;

	private String type;

	private String realTimeUpdateType;

	@JsonProperty(value = "isHostRead")
	private boolean isHostRead;

	@JsonProperty(value = "isGuestRead")
	private boolean isGuestRead;

	private long messageId;

	private long chatRoomId;

	private long senderId;

	private String senderNickname;

	private LocalDateTime createdAt;

	public static MessageResponsePayload emptyBody() {
		return MessageResponsePayload.getBuilder().build();
	}

	public static MessageResponsePayloadBuilder getBuilder() {
		return new MessageResponsePayloadBuilder();
	}

	public static class MessageResponsePayloadBuilder {
		private long id = -1L;
		private String content = "";
		private String type = "";
		private String realTimeUpdateType = "";
		private boolean isHostRead = false;
		private boolean isGuestRead = false;
		private long messageId = -1L;
		private long chatRoomId = -1L;
		private long senderId = -1L;
		private String senderNickname = "";
		private LocalDateTime createdAt = LocalDateTime.now();

		public MessageResponsePayloadBuilder withId(long id) {
			this.id = id;
			return this;
		}

		public MessageResponsePayloadBuilder withContent(String content) {
			this.content = content;
			return this;
		}

		public MessageResponsePayloadBuilder withType(MessageType type) {
			this.type = type.name();
			return this;
		}

		public MessageResponsePayloadBuilder withRealTimeUpdateType(RealTimeUpdateType realTimeUpdateType) {
			this.realTimeUpdateType = realTimeUpdateType.name();
			return this;
		}

		public MessageResponsePayloadBuilder withIsHostRead(boolean isHostRead) {
			this.isHostRead = isHostRead;
			return this;
		}

		public MessageResponsePayloadBuilder withIsGuestRead(boolean isGuestRead) {
			this.isGuestRead = isGuestRead;
			return this;
		}

		public MessageResponsePayloadBuilder withMessageId(long messageId) {
			this.messageId = messageId;
			return this;
		}

		public MessageResponsePayloadBuilder withChatRoomId(long chatRoomId) {
			this.chatRoomId = chatRoomId;
			return this;
		}

		public MessageResponsePayloadBuilder withSenderId(long senderId) {
			this.senderId = senderId;
			return this;
		}

		public MessageResponsePayloadBuilder withSenderNickname(String senderNickname) {
			this.senderNickname = senderNickname;
			return this;
		}

		public MessageResponsePayloadBuilder withCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public MessageResponsePayload build() {
			MessageResponsePayload payload = new MessageResponsePayload();
			payload.setId(id);
			payload.setContent(content);
			payload.setType(type);
			payload.setRealTimeUpdateType(realTimeUpdateType);
			payload.setHostRead(isHostRead);
			payload.setGuestRead(isGuestRead);
			payload.setMessageId(messageId);
			payload.setChatRoomId(chatRoomId);
			payload.setSenderId(senderId);
			payload.setSenderNickname(senderNickname);
			payload.setCreatedAt(createdAt);
			return payload;
		}
	}

	public static MessageResponsePayload buildRealTimeUpdateResponsePayload(RealTimeUpdateType type) {
		return MessageResponsePayload.getBuilder().withRealTimeUpdateType(type).build();
	}

	public static MessageResponsePayload buildChatMessageResponsePayload(Message message) {
		return MessageResponsePayload.getBuilder()
				.withId(message.getId())
				.withContent(message.getContent())
				.withType(message.getType())
				.withRealTimeUpdateType(RealTimeUpdateType.MESSAGE_READ)
				.withIsHostRead(message.isHostRead())
				.withIsGuestRead(message.isGuestRead())
				.withMessageId(message.getMessageId())
				.withChatRoomId(message.getChatRoom().getId())
				.withSenderId(message.getSender().getId())
				.withSenderNickname(message.getSender().getNickname())
				.withCreatedAt(message.getCreatedAt())
				.build();
	}

	public static List<MessageResponsePayload> buildMessageResponsePayload(MessageRepository messageRepository, List<Message> messages, boolean isHost) {
		return messages.stream()
				.map(message -> {
					message.readMessage(false, isHost);
					messageRepository.save(message);
					return MessageResponsePayload.buildChatMessageResponsePayload(message);
				})
				.collect(Collectors.toList());
	}
}
