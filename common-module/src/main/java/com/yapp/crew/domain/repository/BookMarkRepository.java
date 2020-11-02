package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.BookMark;
import com.yapp.crew.domain.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

  List<BookMark> findAll();

  void deleteByUserAndBoard(User user, Board board);
}
