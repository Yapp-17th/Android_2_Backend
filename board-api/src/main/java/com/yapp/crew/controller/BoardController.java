package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.BoardContentResponseDto;
import com.yapp.crew.dto.BoardRequestDto;
import com.yapp.crew.exception.InternalServerErrorException;
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.model.SimpleResponse;
import com.yapp.crew.service.BoardService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping(path = "/v1/board/post", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postBoard(@RequestHeader(value = "Authorization") String token, @RequestBody @Valid BoardRequestDto boardRequestDto) {
    auth.verifyToken(token);
    // TODO: try - catch
    long userId = auth.parseUserIdFromToken(token);
    BoardPostRequiredInfo boardPostRequiredInfo = BoardPostRequiredInfo.build(boardRequestDto);
    SimpleResponse simpleResponse = boardService.postBoard(boardPostRequiredInfo, userId);

    return ResponseEntity.ok().body(simpleResponse);
  }

  @GetMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getBoardContent(@RequestHeader(value = "Authorization") String token, @PathVariable Long boardId) {
    auth.verifyToken(token);
    // TODO: try - catch
    long userId = auth.parseUserIdFromToken(token);
    BoardContentResponseInfo boardContentResponseInfo = boardService.getBoardContent(boardId, userId);
    if (boardContentResponseInfo == null) {
      throw new InternalServerErrorException();
    }

    return ResponseEntity.ok().body(BoardContentResponseDto.build(boardContentResponseInfo));
  }

  @DeleteMapping(path = "/v1/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteBoard(@RequestHeader(value = "Authorization") String token, @PathVariable Long boardId) {
    auth.verifyToken(token);
    // TODO: try - catch
    long userId = auth.parseUserIdFromToken(token);
    SimpleResponse simpleResponse = boardService.deleteBoard(boardId, userId);

    return ResponseEntity.ok().body(simpleResponse);
  }

  public ResponseEntity<?> editBoard(@RequestHeader(value = "Authorization") String token, @PathVariable Long boardId, @RequestBody @Valid BoardRequestDto boardRequestDto) {
    auth.verifyToken(token);
    // TODO: try - catch
    long userId = auth.parseUserIdFromToken(token);
    BoardPostRequiredInfo boardPostRequiredInfo = BoardPostRequiredInfo.build(boardRequestDto);
    BoardContentResponseInfo boardContentResponseInfo = boardService.editBoardContent(boardId, userId, boardPostRequiredInfo);

    return ResponseEntity.ok().body(BoardContentResponseDto.build(boardContentResponseInfo));
  }
}
