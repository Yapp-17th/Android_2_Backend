package com.yapp.crew.payload;

import java.time.LocalDateTime;

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

	private boolean isRead;

	private Long senderId;

	private String senderNickname;

	private LocalDateTime createdAt;
}
