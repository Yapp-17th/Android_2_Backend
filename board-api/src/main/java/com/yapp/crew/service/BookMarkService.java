package com.yapp.crew.service;

import com.yapp.crew.domain.repository.BookMarkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookMarkService {

  private BookMarkRepository bookMarkRepository;

  @Autowired
  public BookMarkService(BookMarkRepository bookMarkRepository) {
    this.bookMarkRepository = bookMarkRepository;
  }
}
