package com.yapp.crew.payload;

import com.yapp.crew.domain.type.MessageType;
import com.yapp.crew.domain.type.RealTimeUpdateType;
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

	private String content;

	private MessageType type;

	private RealTimeUpdateType realTimeUpdateType;

	private Long senderId;

	private Long chatRoomId;

	private Long boardId;
}
