package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.request.BoardInfoRequestDto;
import com.yapp.crew.dto.response.BoardContentSuccessResponseDto;
import com.yapp.crew.dto.response.BoardListSuccessResponseDto;
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardFilter;
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.BoardService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class BoardController {

	private BoardService boardService;
	private Auth auth;

	@Autowired
	public BoardController(BoardService boardService, Auth auth) {
		this.boardService = boardService;
		this.auth = auth;
	}

	@GetMapping(path = "/v1/board", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBoardList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PageableDefault(size = 20) Pageable pageable, @RequestParam(required = false) String sorting, @RequestParam(required = false) List<Long> category,
			@RequestParam(required = false) List<Long> address) {
		long userId = auth.parseUserIdFromToken(token);
		log.info("[GET BOARD LIST] user id:" + userId);
		List<BoardListResponseInfo> boardListResponseInfoList = boardService.getBoardList(BoardFilter.build(sorting, category, address, userId));

		PagedListHolder<BoardListResponseInfo> page = new PagedListHolder<>(boardListResponseInfoList);
		page.setPageSize(pageable.getPageSize());
		page.setPage(pageable.getPageNumber());

		BoardListSuccessResponseDto boardListSuccessResponseDto = BoardListSuccessResponseDto.build(page.getPageList());
		return ResponseEntity.ok().body(boardListSuccessResponseDto);
	}

	@PostMapping(path = "/v1/board", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postBoard(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BoardInfoRequestDto boardInfoRequestDto) {

		long userId = auth.parseUserIdFromToken(token);
		BoardPostRequiredInfo boardPostRequiredInfo = BoardPostRequiredInfo.build(boardInfoRequestDto);
		SimpleResponse simpleResponse = boardService.postBoard(boardPostRequiredInfo, userId);

		return ResponseEntity.ok().body(simpleResponse);
	}

	@GetMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBoardContent(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable Long boardId) {

		long userId = auth.parseUserIdFromToken(token);
		BoardContentResponseInfo boardContentResponseInfo = boardService.getBoardContent(boardId, userId);

		return ResponseEntity.ok().body(BoardContentSuccessResponseDto.build(boardContentResponseInfo));
	}

	@DeleteMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteBoard(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable Long boardId) {

		long userId = auth.parseUserIdFromToken(token);
		SimpleResponse simpleResponse = boardService.deleteBoard(boardId, userId);

		return ResponseEntity.ok().body(simpleResponse);
	}

	@PutMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editBoard(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable Long boardId, @RequestBody @Valid BoardInfoRequestDto boardInfoRequestDto) {

		long userId = auth.parseUserIdFromToken(token);
		BoardPostRequiredInfo boardPostRequiredInfo = BoardPostRequiredInfo.build(boardInfoRequestDto);
		BoardContentResponseInfo boardContentResponseInfo = boardService.editBoardContent(boardId, userId, boardPostRequiredInfo);

		return ResponseEntity.ok().body(BoardContentSuccessResponseDto.build(boardContentResponseInfo));
	}
}
