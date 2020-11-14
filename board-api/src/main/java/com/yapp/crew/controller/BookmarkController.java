package com.yapp.crew.controller;

import com.yapp.crew.dto.request.BoardIdRequestDto;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.BookMarkService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class BookmarkController {

	private BookMarkService bookMarkService;

	@Autowired
	public BookmarkController(BookMarkService bookMarkService) {
		this.bookMarkService = bookMarkService;
	}

	@PostMapping(path = "/v1/board/bookmark", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postBoard(
			@RequestHeader(value = "userId") Long userId,
			@RequestBody @Valid BoardIdRequestDto boardIdRequestDto
	) {
		SimpleResponse simpleResponse = bookMarkService.createBookMark(boardIdRequestDto.getBoardId(), userId);

		return ResponseEntity.ok().body(simpleResponse);
	}

	@DeleteMapping(path = "/v1/board/{boardId}/bookmark", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteBoard(
			@RequestHeader(value = "userId") Long userId,
			@PathVariable(name = "boardId") Long boardId
	) {
		SimpleResponse simpleResponse = bookMarkService.deleteBookMark(boardId, userId);

		return ResponseEntity.ok().body(simpleResponse);
	}
}
