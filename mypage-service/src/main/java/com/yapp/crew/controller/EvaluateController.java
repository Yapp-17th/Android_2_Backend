package com.yapp.crew.controller;

import com.yapp.crew.dto.request.UserReportRequestDto;
import com.yapp.crew.dto.response.EvaluateListResponseDto;
import com.yapp.crew.model.EvaluateListInfo;
import com.yapp.crew.model.UserReportRequest;
import com.yapp.crew.network.dto.SimpleResponseDto;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.EvaluateService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j(topic = "Evaluate Controller")
@RestController
public class EvaluateController {

	private EvaluateService evaluateService;

	@Autowired
	public EvaluateController(EvaluateService evaluateService) {
		this.evaluateService = evaluateService;
	}

	@GetMapping(path = "/v1/user/my-profile/history/{boardId}/evaluate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEvaluateList(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "boardId") long boardId
	) {
		log.info("Get evaluate list -> userId: {}, boardId: {}", userId, boardId);
		List<EvaluateListInfo> evaluateListInfos = evaluateService.getEvaluateList(userId, boardId);
		EvaluateListResponseDto evaluateListResponseDto = EvaluateListResponseDto.build(evaluateListInfos);

		return ResponseEntity.ok().body(evaluateListResponseDto);
	}

	@PutMapping(path = "/v1/user/my-profile/history/{boardId}/evaluate/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> putEvaluate(
			@RequestHeader(value = "userId") long evaluateId,
			@PathVariable(name = "boardId") long boardId,
			@PathVariable(name = "userId") long userId,
			@RequestParam boolean isLike
	) {
		log.info("Put evaluate -> evaluatedId: {}, boardId: {}, userId: {}, isLike: {}", evaluateId, boardId, userId, isLike);
		SimpleResponse simpleResponse = evaluateService.putUserEvaluation(evaluateId, userId, boardId, isLike);

		return ResponseEntity.ok().body(SimpleResponseDto.build(simpleResponse));
	}

	@PostMapping(path = "/v1/user/my-profile/history/evaluate/report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postEvaluateReport(
			@RequestHeader(value = "userId") long userId,
			@RequestBody @Valid UserReportRequestDto reportRequestDto
	) {
		log.info("Post evaluated Report -> userId: {}, payload: {}", userId, reportRequestDto);
		UserReportRequest userReportRequest = UserReportRequest.build(userId, reportRequestDto);
		SimpleResponse simpleResponse = evaluateService.postUserReport(userReportRequest);

		return ResponseEntity.ok().body(SimpleResponseDto.build(simpleResponse));
	}
}
