package com.yapp.crew.model;

import com.yapp.crew.dto.request.UserReportRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserReportRequest {

	private Long reportedId;
	private Long reportId;
	private Long type;
	private String content;

	public static UserReportRequest build(long userId, UserReportRequestDto userReportRequestDto) {
		UserReportRequest userReportRequest = new UserReportRequest();
		userReportRequest.reportId = userId;
		userReportRequest.reportedId = userReportRequestDto.getUserId();
		userReportRequest.type = userReportRequestDto.getReportType();
		userReportRequest.content = userReportRequestDto.getContent();

		return userReportRequest;
	}
}
