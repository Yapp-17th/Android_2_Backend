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
public class ApplyRequestPayload {

	@NotNull
	private long chatRoomId;

	private long boardId;

	private long applierId;

	@Override
	public String toString() {
		return "ApplyRequestPayload{" +
				"chatRoomId=" + chatRoomId +
				", boardId=" + boardId +
				", applierId=" + applierId +
				'}';
	}
}
