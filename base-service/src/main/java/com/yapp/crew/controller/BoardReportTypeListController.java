package com.yapp.crew.controller;

import com.yapp.crew.dto.EnumListResponseDto;
import com.yapp.crew.utils.EnumToList;
import com.yapp.crew.utils.ResponseDomain;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardReportTypeListController {

	@GetMapping(path = "/v1/board/report/type")
	public ResponseEntity<?> getBoardReportTypeList() {

		List<String> boardReportTypeList = EnumToList.boardReportTypeEnumToList();
		EnumListResponseDto enumListResponseDto = EnumListResponseDto.pass(ResponseDomain.BOARD_REPORT_TYPE.getName(), boardReportTypeList);

		return ResponseEntity.ok().body(enumListResponseDto);
	}
}
