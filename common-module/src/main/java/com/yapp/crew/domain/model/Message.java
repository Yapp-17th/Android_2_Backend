package com.yapp.crew.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yapp.crew.domain.type.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false, length = 500)
	private String content;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private MessageType type;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "message_id", nullable = false)
	private long messageId;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "is_host_read", nullable = false)
	private boolean isHostRead;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "is_guest_read", nullable = false)
	private boolean isGuestRead;

	@JsonBackReference
	@Setter(value = AccessLevel.PRIVATE)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User sender;

	@JsonBackReference
	@Setter(value = AccessLevel.PROTECTED)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ChatRoom chatRoom;

	public void readMessage(boolean readBoth, boolean isHost) {
		if (readBoth) {
			setHostRead(true);
			setGuestRead(true);
			return;
		}
		if (isHost) {
			setHostRead(true);
			return;
		}
		setGuestRead(true);
	}

	public static Message buildChatMessage(String content, MessageType type, long messageId, User sender, ChatRoom chatRoom) {
		return Message.getBuilder()
				.withContent(content)
				.withType(type)
				.withMessageId(messageId)
				.withIsHostRead(false)
				.withIsGuestRead(false)
				.withSender(sender)
				.withChatRoom(chatRoom)
				.build();
	}

	public static MessageBuilder getBuilder() {
		return new MessageBuilder();
	}

	public static class MessageBuilder {
		private String content;
		private MessageType type;
		private long messageId;
		private boolean isHostRead;
		private boolean isGuestRead;
		private User sender;
		private ChatRoom chatRoom;

		public MessageBuilder withContent(String content) {
			this.content = content;
			return this;
		}

		public MessageBuilder withType(MessageType type) {
			this.type = type;
			return this;
		}

		public MessageBuilder withMessageId(long messageId) {
			this.messageId = messageId;
			return this;
		}

		public MessageBuilder withIsHostRead(boolean isHostRead) {
			this.isHostRead = isHostRead;
			return this;
		}

		public MessageBuilder withIsGuestRead(boolean isGuestRead) {
			this.isGuestRead = isGuestRead;
			return this;
		}

		public MessageBuilder withSender(User sender) {
			this.sender = sender;
			return this;
		}

		public MessageBuilder withChatRoom(ChatRoom chatRoom) {
			this.chatRoom = chatRoom;
			return this;
		}

		public Message build() {
			Message message = new Message();
			message.setContent(content);
			message.setType(type);
			message.setMessageId(messageId);
			message.setHostRead(isHostRead);
			message.setGuestRead(isGuestRead);
			message.setSender(sender);
			message.setChatRoom(chatRoom);
			return message;
		}
	}
}
