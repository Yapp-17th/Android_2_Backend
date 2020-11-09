package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Board;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

	List<Board> findAll();

	List<Board> findAllByUserId(Long userId);

	Optional<Board> findBoardById(Long boardId);

	Board save(Board board);

	List<Board> findByContentIsContaining(String keyWord);

	List<Board> findAllByStartsAtBetween(LocalDateTime starts, LocalDateTime ends);
}
