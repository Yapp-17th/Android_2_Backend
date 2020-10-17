package com.yapp.crew.domain.repository;

import java.util.List;

import com.yapp.crew.domain.model.Board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

  List<Board> findAll();

  List<Board> findAllByUserId(Long userId);

  Board save(Board board);
}
