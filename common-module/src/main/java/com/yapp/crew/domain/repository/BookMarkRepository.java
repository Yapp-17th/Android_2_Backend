package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.BookMark;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

  List<BookMark> findAll();
}
