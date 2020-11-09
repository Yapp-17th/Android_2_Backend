package com.yapp.crew.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserReportRequestDto {

	@NotNull
	private Long boardId;
	@NotNull
	private Long reportType;
	private String content;
}
