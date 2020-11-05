package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.request.BoardReportRequestDto;
import com.yapp.crew.model.BoardReport;
import com.yapp.crew.model.SimpleResponse;
import com.yapp.crew.service.ReportService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
public class ReportController {

  private ReportService reportService;
  private Auth auth;

  @Autowired
  public ReportController(ReportService reportService, Auth auth) {
    this.reportService = reportService;
    this.auth = auth;
  }

  @PostMapping(path = "/v1/board/report", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postBoard(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BoardReportRequestDto boardReportRequestDto) {

    long userId = auth.parseUserIdFromToken(token);
    SimpleResponse simpleResponse = reportService.postBoardReport(BoardReport.build(boardReportRequestDto, userId));

    return ResponseEntity.ok().body(simpleResponse);
  }
}
