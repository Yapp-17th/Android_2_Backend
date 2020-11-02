package com.yapp.crew.payload;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponsePayload {

	private Long id;

	private String content;

	private MessageType type;

	private boolean isHostRead;

	private boolean isGuestRead;

	private Long senderId;

	private String senderNickname;

	@JsonInclude(value = Include.NON_NULL)
	private Long likes;

	@JsonInclude(value = Include.NON_NULL)
	private Long dislikes;

	private LocalDateTime createdAt;

	public static MessageResponsePayload buildChatMessageResponsePayload(Message message) {
		return MessageResponsePayload.builder()
						.id(message.getId())
						.content(message.getContent())
						.type(message.getType())
						.isHostRead(message.isHostRead())
						.isGuestRead(message.isGuestRead())
						.senderId(message.getSender().getId())
						.senderNickname(message.getSender().getNickname())
						.createdAt(message.getCreatedAt())
						.build();
	}

	public static MessageResponsePayload buildProfileMessageResponsePayload(Message message) {
		return MessageResponsePayload.builder()
						.id(message.getId())
						.content(message.getContent())
						.type(message.getType())
						.isHostRead(message.isHostRead())
						.isGuestRead(message.isGuestRead())
						.senderId(message.getSender().getId())
						.senderNickname(message.getSender().getNickname())
						.likes(message.getProfileMessage().getLikes())
						.dislikes(message.getProfileMessage().getDislikes())
						.createdAt(message.getCreatedAt())
						.build();
	}
}
