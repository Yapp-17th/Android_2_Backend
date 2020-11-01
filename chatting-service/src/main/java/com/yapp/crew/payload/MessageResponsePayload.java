package com.yapp.crew.payload;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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

	@JsonInclude(value = Include.NON_NULL)
	private String label;

	@JsonInclude(value = Include.NON_NULL)
	private String buttonLabel;

	private LocalDateTime createdAt;
}
