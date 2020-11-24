package com.yapp.crew.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardReportRequestDto {

	@NotNull
	private Long boardId;

	@NotNull
	private Long reportType;

	private String content;

	@Override
	public String toString() {
		return "BoardReportRequestDto{" +
				"boardId=" + boardId +
				", reportType=" + reportType +
				", content='" + content + '\'' +
				'}';
	}
}
