package com.yapp.crew.service;

import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardSearchAndFilterRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.model.BoardListResponseInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SearchService {

	private final UserRepository userRepository;
	private final BoardSearchAndFilterRepository boardSearchAndFilterRepository;

	@Autowired
	public SearchService(
			UserRepository userRepository,
			BoardSearchAndFilterRepository boardSearchAndFilterRepository
	) {
		this.userRepository = userRepository;
		this.boardSearchAndFilterRepository = boardSearchAndFilterRepository;
	}

	@Transactional
	public List<BoardListResponseInfo> searchBoardList(BoardSearchCondition boardSearchCondition, Pageable pageable) {
		User user = findUserById(boardSearchCondition.getUserId())
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		return searchBoard(boardSearchCondition, pageable).stream()
				.map(board -> BoardListResponseInfo.build(board, user))
				.collect(Collectors.toList());
	}

	private List<Board> searchBoard(BoardSearchCondition boardSearchCondition, Pageable pageable) {
		return boardSearchAndFilterRepository.search(boardSearchCondition, pageable);
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}
}
