package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.request.UserReportRequestDto;
import com.yapp.crew.dto.response.EvaluateListResponseDto;
import com.yapp.crew.model.EvaluateListInfo;
import com.yapp.crew.model.UserReportRequest;
import com.yapp.crew.network.dto.SimpleResponseDto;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.EvaluateService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvaluateController {

	private EvaluateService evaluateService;
	private Auth auth;

	@Autowired
	public EvaluateController(EvaluateService evaluateService, Auth auth) {
		this.evaluateService = evaluateService;
		this.auth = auth;
	}

	@GetMapping(path = "/v1/user/my-profile/history/{boardId}/evaluate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEvaluateList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable Long boardId) {
		long userId = auth.parseUserIdFromToken(token);

		List<EvaluateListInfo> evaluateListInfos = evaluateService.getEvaluateList(userId, boardId);
		EvaluateListResponseDto evaluateListResponseDto = EvaluateListResponseDto.build(evaluateListInfos);

		return ResponseEntity.ok().body(evaluateListResponseDto);
	}

	@PutMapping(path = "/v1/user/my-profile/history/{boardId}/evaluate/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> putEvaluate(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable Long boardId, @PathVariable Long userId, @RequestParam boolean isLike) {
		long evaluateId = auth.parseUserIdFromToken(token);

		SimpleResponse simpleResponse = evaluateService.putUserEvaluation(evaluateId, userId, boardId, isLike);

		return ResponseEntity.ok().body(SimpleResponseDto.build(simpleResponse));
	}

	@PostMapping(path = "/v1/user/my-profile/history/evaluate/report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postEvaluateReport(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid UserReportRequestDto reportRequestDto) {
		long userId = auth.parseUserIdFromToken(token);

		UserReportRequest userReportRequest = UserReportRequest.build(userId, reportRequestDto);
		SimpleResponse simpleResponse = evaluateService.postUserReport(userReportRequest);

		return ResponseEntity.ok().body(SimpleResponseDto.build(simpleResponse));
	}
}
