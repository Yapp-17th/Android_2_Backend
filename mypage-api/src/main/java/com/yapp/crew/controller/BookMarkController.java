package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.response.BookMarkListResponseDto;
import com.yapp.crew.dto.response.UserProfileResponseDto;
import com.yapp.crew.model.BoardListInfo;
import com.yapp.crew.model.UserProfileInfo;
import com.yapp.crew.service.BookMarkService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookMarkController {

	private BookMarkService bookMarkService;
	private Auth auth;

	@Autowired
	public BookMarkController(BookMarkService bookMarkService, Auth auth) {
		this.bookMarkService = bookMarkService;
		this.auth = auth;
	}

	@GetMapping(path = "/v1/user/my-profile/bookmark", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyBookmark(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
		long userId = auth.parseUserIdFromToken(token);

		List<BoardListInfo> boardListInfos = bookMarkService.getBookMarks(userId);
		BookMarkListResponseDto bookMarkListResponseDto = BookMarkListResponseDto.build(boardListInfos);

		return ResponseEntity.ok().body(bookMarkListResponseDto);
	}
}
