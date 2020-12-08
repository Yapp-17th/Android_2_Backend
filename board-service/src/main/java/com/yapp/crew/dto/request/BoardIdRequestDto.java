package com.yapp.crew.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardIdRequestDto {

	@NotNull
	private long boardId;

	@Override
	public String toString() {
		return "BoardIdRequestDto{" +
				"boardId=" + boardId +
				'}';
	}
}
