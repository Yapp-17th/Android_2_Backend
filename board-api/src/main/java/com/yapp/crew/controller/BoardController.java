package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.BoardContentResponseDto;
import com.yapp.crew.dto.BoardFilterRequestDto;
import com.yapp.crew.dto.BoardListResponseDto;
import com.yapp.crew.dto.BoardRequestDto;
import com.yapp.crew.exception.InternalServerErrorException;
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardFilter;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.model.BoardResponseInfo;
import com.yapp.crew.model.SimpleResponse;
import com.yapp.crew.service.BoardService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
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
  public ResponseEntity<?> getBoardList(@RequestHeader(value = "Authorization") String token, @PageableDefault(size = 20, page = 0) Pageable pageable, @RequestBody @Valid BoardFilterRequestDto boardFilterRequestDto) {

    List<BoardResponseInfo> boardResponseInfoList = boardService.getBoardList(new BoardFilter(boardFilterRequestDto));

    PagedListHolder<BoardResponseInfo> page = new PagedListHolder<>(boardResponseInfoList);
    page.setPageSize(pageable.getPageSize());
    page.setPage(pageable.getPageNumber());

    BoardListResponseDto boardListResponseDto = BoardListResponseDto.build(page.getPageList());
    return ResponseEntity.ok().body(boardListResponseDto);
  }

  @PostMapping(path = "/v1/board", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postBoard(@RequestHeader(value = "Authorization") String token, @RequestBody @Valid BoardRequestDto boardRequestDto) {

    long userId = auth.parseUserIdFromToken(token);
    BoardPostRequiredInfo boardPostRequiredInfo = BoardPostRequiredInfo.build(boardRequestDto);
    SimpleResponse simpleResponse = boardService.postBoard(boardPostRequiredInfo, userId);

    return ResponseEntity.ok().body(simpleResponse);
  }

  @GetMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getBoardContent(@RequestHeader(value = "Authorization") String token, @PathVariable Long boardId) {

    long userId = auth.parseUserIdFromToken(token);
    BoardContentResponseInfo boardContentResponseInfo = boardService.getBoardContent(boardId, userId);
    if (boardContentResponseInfo == null) {
      throw new InternalServerErrorException();
    }

    return ResponseEntity.ok().body(BoardContentResponseDto.build(boardContentResponseInfo));
  }

  @DeleteMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteBoard(@RequestHeader(value = "Authorization") String token, @PathVariable Long boardId) {

    long userId = auth.parseUserIdFromToken(token);
    SimpleResponse simpleResponse = boardService.deleteBoard(boardId, userId);

    return ResponseEntity.ok().body(simpleResponse);
  }

  @PutMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> editBoard(@RequestHeader(value = "Authorization") String token, @PathVariable Long boardId, @RequestBody @Valid BoardRequestDto boardRequestDto) {

    long userId = auth.parseUserIdFromToken(token);
    BoardPostRequiredInfo boardPostRequiredInfo = BoardPostRequiredInfo.build(boardRequestDto);
    BoardContentResponseInfo boardContentResponseInfo = boardService.editBoardContent(boardId, userId, boardPostRequiredInfo);

    return ResponseEntity.ok().body(BoardContentResponseDto.build(boardContentResponseInfo));
  }
}
