package com.yapp.crew.payload;

import java.time.LocalDateTime;

import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
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
public class MessageSocketPayload {

	private Long id;

	private String content;

	private MessageType type;

	private boolean isRead;

	private UserPayload user;

	private LocalDateTime createdAt;

	public void setUser(Message message) {
		User sender = message.getSender();
		UserPayload payload = UserPayload.builder()
						.id(sender.getId())
						.nickname(sender.getNickname())
						.username(sender.getUsername())
						.build();
	}
}
