package com.yapp.crew.service;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Report;
import com.yapp.crew.domain.model.Report.ReportBuilder;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.ReportRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.model.BoardReport;
import com.yapp.crew.model.SimpleResponse;
import com.yapp.crew.utils.ResponseMessage;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportService {

  private ReportRepository reportRepository;
  private UserRepository userRepository;
  private BoardRepository boardRepository;

  @Autowired
  public ReportService(ReportRepository reportRepository, UserRepository userRepository, BoardRepository boardRepository) {
    this.reportRepository = reportRepository;
    this.userRepository = userRepository;
    this.boardRepository = boardRepository;
  }

  public SimpleResponse postBoardReport(BoardReport boardReport) {
    User user = findUserById(boardReport.getReporter())
        .orElseThrow(() -> new UserNotFoundException("user not found"));
    Board board = findBoardById(boardReport.getBoardId())
        .orElseThrow(() -> new BoardNotFoundException("board not found"));

    ReportBuilder reportBuilder = Report.getBuilder();
    Report report = reportBuilder
        .withContent(boardReport.getContent())
        .withReporter(user)
        .withReported(board.getUser())
        .withType(boardReport.getReportType())
        .build();
    saveReport(report);
    return SimpleResponse.pass(ResponseMessage.REPORT_SUCCESS.getMessage());
  }

  private void saveReport(Report report) {
    reportRepository.save(report);
    log.info("report 저장 완료");
  }

  private Optional<User> findUserById(Long userId) {
    log.info("user 가져오기 성공");
    return userRepository.findUserById(userId);
  }

  private Optional<Board> findBoardById(Long boardId) {
    log.info("board 가져오기 성공");
    return boardRepository.findBoardById(boardId);
  }
}
