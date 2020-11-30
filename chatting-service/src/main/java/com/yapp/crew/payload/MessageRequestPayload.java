package com.yapp.crew.payload;

import com.yapp.crew.domain.type.MessageType;
import com.yapp.crew.domain.type.RealTimeUpdateType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class MessageRequestPayload {

	@NotBlank
	private String content;

	@NotBlank
	private MessageType type;

	private RealTimeUpdateType realTimeUpdateType;

	@NotNull
	private Long senderId;

	@NotNull
	private Long chatRoomId;

	private Long boardId;
}
