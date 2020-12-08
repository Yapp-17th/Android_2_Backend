package com.yapp.crew.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserReportRequestDto {

	@NotNull
	private long userId;

	@NotNull
	private int reportType;

	private String content;

	@Override
	public String toString() {
		return "UserReportRequestDto{" +
				"userId=" + userId +
				", reportType=" + reportType +
				", content='" + content + '\'' +
				'}';
	}
}
