package com.yapp.crew.controller;

import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.dto.response.BoardListSuccessResponseDto;
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.service.SearchService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Search Controller")
@CrossOrigin
@RestController
public class SearchController {

	private SearchService searchService;

	@Autowired
	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

	@GetMapping(path = "/v1/board/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBoardList(
			@RequestHeader(value = "userId") long userId,
			@PageableDefault(size = 200, page = 0) Pageable pageable,
			@RequestParam List<String> keyword
	) {
		log.info("Get board list -> userId: {}, pageable: {}, keyword: {}", userId, pageable, keyword);
		List<BoardListResponseInfo> boardList = searchService.searchBoardList(BoardSearchCondition.build(keyword, userId), pageable);

		BoardListSuccessResponseDto boardListSuccessResponseDto = BoardListSuccessResponseDto.build(boardList);
		return ResponseEntity.ok().body(boardListSuccessResponseDto);
	}
}
