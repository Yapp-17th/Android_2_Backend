package com.yapp.crew.service;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.BookMark;
import com.yapp.crew.domain.model.BookMark.BookMarkBuilder;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.BookMarkRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.exception.InternalServerErrorException;
import com.yapp.crew.model.SimpleResponse;
import com.yapp.crew.utils.ResponseMessage;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookMarkService {

  private BookMarkRepository bookMarkRepository;
  private BoardRepository boardRepository;
  private UserRepository userRepository;

  @Autowired
  public BookMarkService(BookMarkRepository bookMarkRepository, BoardRepository boardRepository, UserRepository userRepository) {
    this.bookMarkRepository = bookMarkRepository;
    this.boardRepository = boardRepository;
    this.userRepository = userRepository;
  }

  public SimpleResponse createBookMark(Long boardId, Long userId) {
    Board board = findBoardById(boardId)
        .orElseThrow(InternalServerErrorException::new);
    User user = findUserById(userId)
        .orElseThrow(InternalServerErrorException::new);

    saveBookMark(board, user);
    return SimpleResponse.pass(ResponseMessage.BOOKMARK_POST_SUCCESS.getMessage());
  }

  public SimpleResponse deleteBookMark(Long boardId, Long userId) {
    Board board = findBoardById(boardId)
        .orElseThrow(InternalServerErrorException::new);
    User user = findUserById(userId)
        .orElseThrow(InternalServerErrorException::new);

    deleteBookMark(board, user);
    return SimpleResponse.pass(ResponseMessage.BOOKMARK_DELETE_SUCCESS.getMessage());
  }

  private void deleteBookMark(Board board, User user) {
    bookMarkRepository.deleteByUserAndBoard(user, board);
    log.info("북마크 삭제 완료");
  }

  private void saveBookMark(Board board, User user) {
    BookMarkBuilder bookMarkBuilder = BookMark.getBuilder();
    BookMark bookMark = bookMarkBuilder
        .withUser(user)
        .withBoard(board)
        .build();

    user.addBookMark(bookMark);
    board.addBookMark(bookMark);

    bookMarkRepository.save(bookMark);
  }

  private Optional<Board> findBoardById(Long boardId) {
    log.info("board 가져오기 성공");
    return boardRepository.findBoardById(boardId);
  }

  private Optional<User> findUserById(Long userId) {
    log.info("user 가져오기 성공");
    return userRepository.findUserById(userId);
  }

  private Optional<BookMark> findBookMarkById(Long bookMarkId) {
    log.info("user 가져오기 성공");
    return bookMarkRepository.findById(bookMarkId);
  }
}
