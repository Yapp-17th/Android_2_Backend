package com.yapp.crew.service;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.HiddenBoard;
import com.yapp.crew.domain.model.HiddenBoard.HiddenBoardBuilder;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.HiddenBoardRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.network.model.SimpleResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HiddenBoardService {

	private HiddenBoardRepository hiddenBoardRepository;
	private BoardRepository boardRepository;
	private UserRepository userRepository;

	@Autowired
	public HiddenBoardService(HiddenBoardRepository hiddenBoardRepository, BoardRepository boardRepository, UserRepository userRepository) {
		this.hiddenBoardRepository = hiddenBoardRepository;
		this.boardRepository = boardRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public SimpleResponse createHiddenBoard(long boardId, long userId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		HiddenBoardBuilder hiddenBoardBuilder = HiddenBoard.getBuilder();
		HiddenBoard hiddenBoard = hiddenBoardBuilder
				.withUser(user)
				.withBoard(board)
				.build();
		
		board.addHiddenBoard(hiddenBoard);
		user.addHiddenBoard(hiddenBoard);
		saveHiddenBoard(hiddenBoard);

		return SimpleResponse.pass(ResponseType.HIDDEN_SUCCESS);
	}

	private void saveHiddenBoard(HiddenBoard hiddenBoard) {
		hiddenBoardRepository.save(hiddenBoard);
	}

	private Optional<Board> findBoardById(long boardId) {
		return boardRepository.findBoardById(boardId).filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode());
	}

	private Optional<User> findUserById(long userId) {
		return userRepository.findUserById(userId);
	}
}
