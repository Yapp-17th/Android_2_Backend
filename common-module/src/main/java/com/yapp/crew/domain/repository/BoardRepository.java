package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Board;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

	Optional<Board> findBoardById(long boardId);

	List<Board> findAll();

	List<Board> findAllByStartsAtBetween(LocalDateTime starts, LocalDateTime ends);

	Board save(Board board);
}
