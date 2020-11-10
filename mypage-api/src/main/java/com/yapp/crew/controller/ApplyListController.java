package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.response.ApplyListResponseDto;
import com.yapp.crew.model.ApplyListInfo;
import com.yapp.crew.service.ApplyListService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplyListController {

	private ApplyListService applyListService;
	private Auth auth;

	@Autowired
	public ApplyListController(ApplyListService applyListService, Auth auth) {
		this.applyListService = applyListService;
		this.auth = auth;
	}

	@GetMapping(path = "/v1/user/my-profile/history/{boardId}/applied", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAppliedList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable Long boardId) {
		long hostId = auth.parseUserIdFromToken(token);

		List<ApplyListInfo> applyListInfos = applyListService.getApplyList(hostId, boardId);
		ApplyListResponseDto applyListResponseDto = ApplyListResponseDto.build(applyListInfos);

		return ResponseEntity.ok().body(applyListResponseDto);
	}

}
