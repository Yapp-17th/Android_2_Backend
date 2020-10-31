package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Evaluation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

  Evaluation save(Evaluation evaluation);

  List<Evaluation> findAllByUserId(Long userId);

  List<Evaluation> findAllByBoardId(Long boardId);
}
