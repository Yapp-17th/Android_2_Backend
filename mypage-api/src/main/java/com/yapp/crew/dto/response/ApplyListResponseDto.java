package com.yapp.crew.dto.response;

import com.yapp.crew.model.ApplyListInfo;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApplyListResponseDto {

	private int status = HttpStatus.OK.value();
	private boolean success = true;
	private String message = "신청자 리스트 조회 성공";
	private List<ApplyListInfo> data;

	public static ApplyListResponseDto build(List<ApplyListInfo> data) {
		ApplyListResponseDto applyListResponseDto = new ApplyListResponseDto();
		applyListResponseDto.data = data;

		return applyListResponseDto;
	}
}
