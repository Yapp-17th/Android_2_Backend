package com.yapp.crew.service;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.BookMark;
import com.yapp.crew.domain.model.BookMark.BookMarkBuilder;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.BookMarkRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.network.model.SimpleResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public SimpleResponse createBookMark(Long boardId, Long userId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		saveBookMark(board, user);
		return SimpleResponse.pass(ResponseType.BOOKMARK_POST_SUCCESS);
	}

	@Transactional
	public SimpleResponse deleteBookMark(Long boardId, Long userId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		deleteBookMark(board, user);
		return SimpleResponse.pass(ResponseType.BOOKMARK_DELETE_SUCCESS);
	}

	private void deleteBookMark(Board board, User user) {
		bookMarkRepository.deleteByUserAndBoard(user, board);
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
		return boardRepository.findBoardById(boardId).filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode());
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}
}
