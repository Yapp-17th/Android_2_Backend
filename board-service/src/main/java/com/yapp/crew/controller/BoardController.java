package com.yapp.crew.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.dto.request.BoardInfoRequestDto;
import com.yapp.crew.dto.response.BoardContentSuccessResponseDto;
import com.yapp.crew.dto.response.BoardListSuccessResponseDto;
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.BoardService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@Slf4j(topic = "Board Service")
@CrossOrigin
@RestController
public class BoardController {

	private BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping(path = "/v1/board", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBoardList(
			@RequestHeader(value = "userId") long userId,
			@PageableDefault(size = 200) Pageable pageable,
			@RequestParam(required = false) String sorting,
			@RequestParam List<Long> category,
			@RequestParam List<Long> address
	) {
		log.info("Get board list -> userId: {}, pageable: {}, sorting: {}, category: {}, address: {}", userId, pageable, sorting, category, address);
		List<BoardListResponseInfo> boardList = boardService.getBoardList(BoardFilterCondition.build(sorting, category, address, userId), pageable);

		BoardListSuccessResponseDto boardListSuccessResponseDto = BoardListSuccessResponseDto.build(boardList);
		return ResponseEntity.ok().body(boardListSuccessResponseDto);
	}

	@PostMapping(path = "/v1/board", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postBoard(
			@RequestHeader(value = "userId") long userId,
			@RequestBody @Valid BoardInfoRequestDto boardInfoRequestDto
	) {
		log.info("Post board -> userId: {}, payload: {}", userId, boardInfoRequestDto);
		BoardPostRequiredInfo boardPostRequiredInfo = BoardPostRequiredInfo.build(boardInfoRequestDto);
		SimpleResponse simpleResponse = boardService.postBoard(boardPostRequiredInfo, userId);

		return ResponseEntity.ok().body(simpleResponse);
	}

	@GetMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBoardContent(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "boardId") long boardId
	) {
		log.info("Get board content -> userId: {}, boardId: {}", userId, boardId);
		BoardContentResponseInfo boardContentResponseInfo = boardService.getBoardContent(boardId, userId);

		return ResponseEntity.ok().body(BoardContentSuccessResponseDto.build(boardContentResponseInfo));
	}

	@DeleteMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteBoard(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "boardId") long boardId
	) throws JsonProcessingException {
		log.info("Delete board -> userId: {}, boardId: {}", userId, boardId);
		SimpleResponse simpleResponse = boardService.deleteBoard(boardId, userId);

		return ResponseEntity.ok().body(simpleResponse);
	}

	@PutMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editBoard(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "boardId") long boardId,
			@RequestBody @Valid BoardInfoRequestDto boardInfoRequestDto
	) {
		log.info("Edit board -> userId: {}, boardId: {}, payload: {}", userId, boardId, boardInfoRequestDto);
		BoardPostRequiredInfo boardPostRequiredInfo = BoardPostRequiredInfo.build(boardInfoRequestDto);
		BoardContentResponseInfo boardContentResponseInfo = boardService.editBoardContent(boardId, userId, boardPostRequiredInfo);

		return ResponseEntity.ok().body(BoardContentSuccessResponseDto.build(boardContentResponseInfo));
	}
}
