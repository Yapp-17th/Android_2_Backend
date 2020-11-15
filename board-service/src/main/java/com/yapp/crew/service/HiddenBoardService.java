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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
	public SimpleResponse createHiddenBoard(Long boardId, Long userId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));

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
		log.info("hidden board 저장 성공");
		hiddenBoardRepository.save(hiddenBoard);
	}

	private Optional<Board> findBoardById(Long boardId) {
		log.info("board 가져오기 성공");
		return boardRepository.findBoardById(boardId).filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode());
	}

	private Optional<User> findUserById(Long userId) {
		log.info("user 가져오기 성공");
		return userRepository.findUserById(userId);
	}
}
