package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

  List<Board> findAll();

  List<Board> findAllByUserId(Long userId);

  Board save(Board board);
}
