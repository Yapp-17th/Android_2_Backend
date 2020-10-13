package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

  List<Board> findAll();

  List<Board> findAllByUserId(Long userId);

  // List<Board> findAllByCategory(List<String> categoryList);
  // List<Board> findAllByAddress(List<String> addressList);
  // List<Board> findAllByCategoryAndAddress(List<String> categoryList, List<String> addressList);

  Board save(Board board);
}
