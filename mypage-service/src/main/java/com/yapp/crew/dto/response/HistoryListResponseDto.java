package com.yapp.crew.dto.response;

import com.yapp.crew.model.HistoryListInfo;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class HistoryListResponseDto {

	private Integer status = HttpStatus.OK.value();
	private Boolean success = true;
	private String message = "히스토리 조회 성공";
	private String type;
	private List<HistoryListInfo> data;

	public static HistoryListResponseDto build(String type, List<HistoryListInfo> data) {
		HistoryListResponseDto historyListResponseDto = new HistoryListResponseDto();
		historyListResponseDto.type = type;
		historyListResponseDto.data = data;

		return historyListResponseDto;
	}
}
