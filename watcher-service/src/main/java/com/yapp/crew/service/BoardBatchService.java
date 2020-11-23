package com.yapp.crew.service;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.Evaluation.EvaluationBuilder;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.status.AppliedStatus;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BoardBatchService {

	private EvaluationRepository evaluationRepository;
	private EntityManager entityManager;

	@Autowired
	public BoardBatchService(EvaluationRepository evaluationRepository, EntityManager entityManager) {
		this.evaluationRepository = evaluationRepository;
		this.entityManager = entityManager;
	}

	void updateBoardFinishedAll(List<Board> boardList) {
		saveAll(boardList);
		boardList.forEach(Board::finishBoard);
		entityManager.flush();
	}

	void saveAll(List<Board> boardList) {
		EvaluationBuilder evaluationBuilder = Evaluation.getBuilder();

		for (Board board : boardList) {
			List<Long> userIds = board.getAppliedUsers().stream()
					.filter(appliedUser -> appliedUser.getStatus() == AppliedStatus.APPROVED)
					.map(appliedUser -> appliedUser.getUser().getId())
					.collect(Collectors.toList());

			for (int i = 0; i < userIds.size(); i++) {
				for (int j = 0; j < userIds.size(); j++) {
					if (i == j) {
						continue;
					}

					Evaluation evaluation = evaluationBuilder
							.withBoard(board)
							.withEvaluateId(userIds.get(i))
							.withEvaluateId(userIds.get(j))
							.withIsDislike(false)
							.withIsLike(false)
							.build();
					evaluationRepository.save(evaluation);
				}
			}
		}
	}
}
