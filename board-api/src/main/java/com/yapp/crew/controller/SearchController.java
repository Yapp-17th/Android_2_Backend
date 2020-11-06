package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.response.BoardListSuccessResponseDto;
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.model.BoardSearch;
import com.yapp.crew.service.SearchService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class SearchController {

	private SearchService searchService;
	private Auth auth;

	@Autowired
	public SearchController(SearchService searchService, Auth auth) {
		this.searchService = searchService;
		this.auth = auth;
	}

	@GetMapping(path = "/v1/board/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBoardList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PageableDefault(size = 20, page = 0) Pageable pageable, @RequestParam List<String> keyword) {
		Long userId = auth.parseUserIdFromToken(token);
		List<BoardListResponseInfo> boardListResponseInfoList = searchService.searchBoardList(BoardSearch.build(keyword, userId));

		PagedListHolder<BoardListResponseInfo> page = new PagedListHolder<>(boardListResponseInfoList);
		page.setPageSize(pageable.getPageSize());
		page.setPage(pageable.getPageNumber());

		BoardListSuccessResponseDto boardListSuccessResponseDto = BoardListSuccessResponseDto.build(page.getPageList());
		return ResponseEntity.ok().body(boardListSuccessResponseDto);
	}
}
