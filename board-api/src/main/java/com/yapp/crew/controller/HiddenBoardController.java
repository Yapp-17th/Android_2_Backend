package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.request.BoardIdRequestDto;
import com.yapp.crew.model.SimpleResponse;
import com.yapp.crew.service.HiddenBoardService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class HiddenBoardController {

  private HiddenBoardService hiddenBoardService;
  private Auth auth;

  @Autowired
  public HiddenBoardController(HiddenBoardService hiddenBoardService, Auth auth) {
    this.hiddenBoardService = hiddenBoardService;
    this.auth = auth;
  }

  @PostMapping(path = "/v1/board/hidden", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postBoard(@RequestHeader(value = "Authorization") String token, @RequestBody @Valid BoardIdRequestDto boardIdRequestDto) {

    long userId = auth.parseUserIdFromToken(token);
    SimpleResponse simpleResponse = hiddenBoardService.createHiddenBoard(boardIdRequestDto.getBoardId(), userId);

    return ResponseEntity.ok().body(simpleResponse);
  }
}