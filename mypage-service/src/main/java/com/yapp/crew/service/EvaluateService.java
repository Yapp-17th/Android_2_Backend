package com.yapp.crew.service;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.Evaluation.EvaluationBuilder;
import com.yapp.crew.domain.model.Report;
import com.yapp.crew.domain.model.Report.ReportBuilder;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.ReportRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.type.ReportType;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.EvaluateListInfo;
import com.yapp.crew.model.UserReportRequest;
import com.yapp.crew.network.model.SimpleResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EvaluateService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final ReportRepository reportRepository;
	private final EvaluationRepository evaluationRepository;

	@Autowired
	public EvaluateService(UserRepository userRepository, BoardRepository boardRepository, ReportRepository reportRepository, EvaluationRepository evaluationRepository) {
		this.userRepository = userRepository;
		this.boardRepository = boardRepository;
		this.reportRepository = reportRepository;
		this.evaluationRepository = evaluationRepository;
	}

	@Transactional
	public List<EvaluateListInfo> getEvaluateList(long evaluateId, long boardId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		Set<Evaluation> evaluations = board.getEvaluations().stream().filter(evaluation -> evaluation.getEvaluateId() == evaluateId).collect(Collectors.toSet());

		return evaluations.stream()
				.filter(evaluation -> findUserById(evaluation.getEvaluatedId()).orElse(null) != null)
				.map(evaluation -> EvaluateListInfo.build(evaluation, findUserById(evaluation.getEvaluatedId()).get(), board))
				.sorted(Comparator.comparing(EvaluateListInfo::isHost))
				.collect(Collectors.toList());
	}

	@Transactional
	public SimpleResponse putUserEvaluation(long evaluateId, long evaluatedId, long boardId, boolean isLike) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		Optional<Evaluation> existingEvaluate = board.getEvaluations().stream().filter(evaluation -> evaluation.getEvaluateId() == evaluateId && evaluation.getEvaluatedId() == evaluatedId).findFirst();
		if (existingEvaluate.isPresent()) {
			updateEvaluation(existingEvaluate.get(), isLike);
		} else {
			insertEvaluation(board, evaluateId, evaluatedId, isLike);
		}
		return SimpleResponse.pass(ResponseType.EVALUATE_SUCCESS);
	}

	@Transactional
	public SimpleResponse postUserReport(UserReportRequest userReportRequest) {
		User reporter = findUserById(userReportRequest.getReportId())
				.orElseThrow(() -> new UserNotFoundException(userReportRequest.getReportId()));

		User reported = findUserById(userReportRequest.getReportedId())
				.orElseThrow(() -> new UserNotFoundException(userReportRequest.getReportedId()));

		ReportBuilder reportBuilder = Report.getBuilder();
		Report report = reportBuilder
				.withReporter(reporter)
				.withReported(reported)
				.withType(ReportType.valueOfCode(userReportRequest.getType()))
				.withContent(userReportRequest.getContent())
				.build();

		saveReport(report);
		updateUserStatus(reported);
		return SimpleResponse.pass(ResponseType.REPORT_SUCCESS);
	}

	private Optional<User> findUserById(long userId) {
		return userRepository.findUserById(userId);
	}

	private Optional<Board> findBoardById(long boardId) {
		return boardRepository.findBoardById(boardId);
	}

	private void saveReport(Report report) {
		reportRepository.save(report);
	}

	private void updateEvaluation(Evaluation evaluation, boolean isLike) {
		evaluation.evaluate(isLike);
		saveEvaluation(evaluation);
	}

	private void insertEvaluation(Board board, long evaluateId, long evaluatedId, boolean isLike) {
		EvaluationBuilder evaluationBuilder = Evaluation.getBuilder();
		Evaluation evaluation = evaluationBuilder
				.withEvaluatedId(evaluateId)
				.withEvaluateId(evaluatedId)
				.withIsLike(isLike)
				.withIsDislike(!isLike)
				.withBoard(board)
				.build();

		evaluationRepository.save(evaluation);
	}

	private void saveEvaluation(Evaluation evaluation) {
		evaluationRepository.save(evaluation);
	}

	private void updateUserStatus(User userReported) {
		if (userReported.calculateReportedPoint() >= 10) {
			userReported.setUserStatusSuspended();
		}
		userRepository.save(userReported);
	}
}
