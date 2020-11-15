package com.yapp.crew.dto.response;

import com.yapp.crew.model.EvaluateListInfo;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class EvaluateListResponseDto {

	private int status = HttpStatus.OK.value();
	private boolean success = true;
	private String message = "평가 리스트 조회 성공";
	private List<EvaluateListInfo> data;

	public static EvaluateListResponseDto build(List<EvaluateListInfo> data) {
		EvaluateListResponseDto evaluateListResponseDto = new EvaluateListResponseDto();
		evaluateListResponseDto.data = data;

		return evaluateListResponseDto;
	}
}
