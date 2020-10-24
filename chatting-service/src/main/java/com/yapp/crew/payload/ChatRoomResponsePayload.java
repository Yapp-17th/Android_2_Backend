package com.yapp.crew.payload;

import java.time.LocalDateTime;
import java.util.List;

import com.yapp.crew.domain.status.ChatRoomStatus;
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
public class ChatRoomResponsePayload {

	private Long id;

	private Long hostId;

	private Long guestId;

	private Long boardId;

	private ChatRoomStatus status;

	private LocalDateTime createdAt;

	private MessageResponsePayload lastMessage;
}
