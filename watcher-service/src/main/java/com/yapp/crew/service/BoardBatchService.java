package com.yapp.crew.service;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.Evaluation.EvaluationBuilder;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.status.AppliedStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "Board Batch Service")
@Service
public class BoardBatchService {

	private EvaluationRepository evaluationRepository;
	private BoardRepository boardRepository;

	@Autowired
	public BoardBatchService(EvaluationRepository evaluationRepository, BoardRepository boardRepository) {
		this.evaluationRepository = evaluationRepository;
		this.boardRepository = boardRepository;
	}

	@Transactional
	void updateBoardFinishedAll(List<Board> boardList) {
		boardList.forEach(Board::finishBoard);
		boardRepository.saveAll(boardList);
		log.info("Successfully updated board");
	}

	@Retryable(maxAttempts = 5)
	@Transactional
	void saveEvaluationList(Board board) {
		EvaluationBuilder evaluationBuilder = Evaluation.getBuilder();

		List<Long> userIds = board.getAppliedUsers().stream()
				.filter(appliedUser -> appliedUser.getStatus() == AppliedStatus.APPROVED)
				.map(appliedUser -> appliedUser.getUser().getId())
				.collect(Collectors.toList());
		userIds.add(board.getUser().getId());
		log.info("Board approved user ids -> {}", userIds);

		for (int i = 0; i < userIds.size(); i++) {
			for (int j = 0; j < userIds.size(); j++) {
				if (i == j) {
					continue;
				}

				Evaluation evaluation = evaluationBuilder
						.withBoard(board)
						.withEvaluateId(userIds.get(i))
						.withEvaluatedId(userIds.get(j))
						.withIsDislike(false)
						.withIsLike(false)
						.build();
				log.info("Create evaluation -> {}", evaluation);
				board.addEvaluation(evaluation);
				evaluationRepository.save(evaluation);
			}
		}
		log.info("Successfully created evaluations for boardId -> {}", board.getId());
	}
}
