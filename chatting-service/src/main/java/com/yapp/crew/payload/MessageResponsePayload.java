package com.yapp.crew.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.repository.MessageRepository;
import com.yapp.crew.domain.type.MessageType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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

	private Boolean isHostRead;

	private Boolean isGuestRead;

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

	public static List<MessageResponsePayload> buildMessageResponsePayload(MessageRepository messageRepository, List<Message> messages, boolean isHost) {
		return messages.stream()
						.map(message -> {
							message.readMessage(isHost);

							messageRepository.save(message);

							return MessageResponsePayload.buildChatMessageResponsePayload(message);
						})
						.collect(Collectors.toList());
	}
}
