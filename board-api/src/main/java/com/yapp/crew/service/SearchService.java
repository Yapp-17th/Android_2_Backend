package com.yapp.crew.service;

import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.BaseEntity;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.HiddenBoard;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.BoardSearchAndFilterRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.model.BoardListResponseInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SearchService {

	private BoardSearchAndFilterRepository boardSearchAndFilterRepository;
	private UserRepository userRepository;

	@Autowired
	public SearchService(BoardSearchAndFilterRepository boardSearchAndFilterRepository, UserRepository userRepository) {
		this.boardSearchAndFilterRepository = boardSearchAndFilterRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public List<BoardListResponseInfo> searchBoardList(BoardSearchCondition boardSearchCondition) {
		// TODO: repository에서 DTO로 바로 리턴 하도록
		// TODO: user check 꼭 여기서 해야하는지
		User user = findUserById(boardSearchCondition.getUserId())
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		return searchBoard(boardSearchCondition)
				.stream()
				.map(board -> BoardListResponseInfo.build(board, board.getUser()))
				.collect(Collectors.toList());
	}

	private List<Board> searchBoard(BoardSearchCondition boardSearchCondition) {
		return boardSearchAndFilterRepository.search(boardSearchCondition);
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}
}
