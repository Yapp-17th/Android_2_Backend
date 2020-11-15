package com.yapp.crew.model;

import com.yapp.crew.dto.request.UserReportRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserReportRequest {

	private long reportedId;
	private long reportId;
	private long type;
	private String content;

	public static UserReportRequest build(long userId, UserReportRequestDto userReportRequestDto) {
		UserReportRequest userReportRequest = new UserReportRequest();
		userReportRequest.reportId = userId;
		userReportRequest.reportedId = userReportRequestDto.getUserId();
		userReportRequest.type = userReportRequest.getType();
		userReportRequest.content = userReportRequest.getContent();

		return userReportRequest;
	}
}
