package com.yapp.crew.payload;

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
public class ApproveRequestPayload {

	@NotNull
	private Long chatRoomId;

	private Long boardId;

	private Long hostId;

	@NotNull
	private Long guestId;
}
