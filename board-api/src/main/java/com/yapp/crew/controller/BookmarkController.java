package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.BookMarkRequestDto;
import com.yapp.crew.model.SimpleResponse;
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
  private Auth auth;

  @Autowired
  public BookmarkController(BookMarkService bookMarkService, Auth auth) {
    this.bookMarkService = bookMarkService;
    this.auth = auth;
  }

  @PostMapping(path = "/v1/board/bookMark", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postBoard(@RequestHeader(value = "Authorization") String token, @RequestBody @Valid BookMarkRequestDto bookMarkRequestDto) {
    auth.verifyToken(token);
    // TODO: try - catch
    long userId = auth.parseUserIdFromToken(token);
    SimpleResponse simpleResponse = bookMarkService.createBookMark(bookMarkRequestDto.getBoardId(), userId);

    return ResponseEntity.ok().body(simpleResponse);
  }

  @DeleteMapping(path = "/v1/board/bookMark/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteBoard(@RequestHeader(value = "Authorization") String token, @PathVariable Long boardId) {
    auth.verifyToken(token);
    // TODO: try - catch
    long userId = auth.parseUserIdFromToken(token);
    SimpleResponse simpleResponse = bookMarkService.deleteBookMark(boardId, userId);

    return ResponseEntity.ok().body(simpleResponse);
  }
}
