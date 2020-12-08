package com.yapp.crew.controller;

import com.yapp.crew.dto.response.ApplyListResponseDto;
import com.yapp.crew.model.ApplyListInfo;
import com.yapp.crew.service.ApplyListService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Apply List Controller")
@RestController
public class ApplyListController {

	private ApplyListService applyListService;

	@Autowired
	public ApplyListController(ApplyListService applyListService) {
		this.applyListService = applyListService;
	}

	@GetMapping(path = "/v1/user/my-profile/history/{boardId}/applied", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAppliedList(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "boardId") long boardId
	) {
		log.info("Get applied list -> userId: {}, boardId: {}", userId, boardId);
		List<ApplyListInfo> applyListInfos = applyListService.getApplyList(userId, boardId);
		ApplyListResponseDto applyListResponseDto = ApplyListResponseDto.build(applyListInfos);

		return ResponseEntity.ok().body(applyListResponseDto);
	}
}
