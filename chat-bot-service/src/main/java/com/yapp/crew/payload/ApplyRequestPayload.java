package com.yapp.crew.payload;

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
public class ApplyRequestPayload {

	private Long chatRoomId;

	private Long boardId;

	private Long applierId;
}