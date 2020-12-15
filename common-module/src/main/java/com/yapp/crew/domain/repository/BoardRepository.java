package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Board;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

	Optional<Board> findBoardById(long boardId);

	List<Board> findAll();

	Board save(Board board);

	@Query("SELECT b FROM Board b where b.startsAt >= :startDate AND b.startsAt <= :endDate AND b.status >= 2")
	List<Board> findAllBoardByExpiredStatus(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
