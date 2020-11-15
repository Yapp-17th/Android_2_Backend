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
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.BoardReport;
import com.yapp.crew.network.model.SimpleResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public SimpleResponse postBoardReport(BoardReport boardReport) {
		User reporter = findUserById(boardReport.getReporter())
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		Board board = findBoardById(boardReport.getBoardId())
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		ReportBuilder reportBuilder = Report.getBuilder();
		Report report = reportBuilder
				.withContent(boardReport.getContent())
				.withReporter(reporter)
				.withReported(board.getUser())
				.withType(boardReport.getReportType())
				.build();

		reporter.addReport(report);
		board.getUser().addReported(report);
		saveReport(report);
		updateUserStatus(board.getUser());

		return SimpleResponse.pass(ResponseType.REPORT_SUCCESS);
	}

	private void updateUserStatus(User userReported) {
		if (userReported.calculateReportedPoint() >= 10) {
			userReported.setUserStatusSuspended();
		}
		userRepository.save(userReported);
	}

	private void saveReport(Report report) {
		reportRepository.save(report);
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}

	private Optional<Board> findBoardById(Long boardId) {
		return boardRepository.findBoardById(boardId).filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode());
	}
}
