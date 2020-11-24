package com.yapp.crew.controller;

import com.yapp.crew.dto.request.BoardIdRequestDto;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.HiddenBoardService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Hidden Board Controller")
@CrossOrigin
@RestController
public class HiddenBoardController {

	private HiddenBoardService hiddenBoardService;

	@Autowired
	public HiddenBoardController(HiddenBoardService hiddenBoardService) {
		this.hiddenBoardService = hiddenBoardService;
	}

	@PostMapping(path = "/v1/board/hidden", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postBoard(
			@RequestHeader(value = "userId") Long userId,
			@RequestBody @Valid BoardIdRequestDto boardIdRequestDto
	) {
		log.info("Post board -> userId: {}, payload: {}", userId, boardIdRequestDto);
		SimpleResponse simpleResponse = hiddenBoardService.createHiddenBoard(boardIdRequestDto.getBoardId(), userId);

		return ResponseEntity.ok().body(simpleResponse);
	}
}
