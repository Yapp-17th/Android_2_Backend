package com.yapp.crew.controller;

import com.yapp.crew.dto.response.BookMarkListResponseDto;
import com.yapp.crew.model.BoardListInfo;
import com.yapp.crew.service.BookMarkService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Bookmark Controller")
@RestController
public class BookMarkController {

	private BookMarkService bookMarkService;

	@Autowired
	public BookMarkController(BookMarkService bookMarkService) {
		this.bookMarkService = bookMarkService;
	}

	@GetMapping(path = "/v1/user/my-profile/bookmark", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyBookmark(@RequestHeader(value = "userId") Long userId) {
		log.info("Get my bookmark -> userId: {}", userId);

		List<BoardListInfo> boardListInfos = bookMarkService.getBookMarks(userId);
		BookMarkListResponseDto bookMarkListResponseDto = BookMarkListResponseDto.build(boardListInfos);

		return ResponseEntity.ok().body(bookMarkListResponseDto);
	}
}
