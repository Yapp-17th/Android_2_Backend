package com.yapp.crew.service;

import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.repository.BoardSearchAndFilterRepository;
import com.yapp.crew.model.BoardListResponseInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SearchService {

	private final BoardSearchAndFilterRepository boardSearchAndFilterRepository;

	@Autowired
	public SearchService(BoardSearchAndFilterRepository boardSearchAndFilterRepository) {
		this.boardSearchAndFilterRepository = boardSearchAndFilterRepository;
	}

	@Transactional
	public List<BoardListResponseInfo> searchBoardList(BoardSearchCondition boardSearchCondition, Pageable pageable) {
		return searchBoard(boardSearchCondition, pageable)
				.stream()
				.map(board -> BoardListResponseInfo.build(board, boardSearchCondition.getUserId()))
				.collect(Collectors.toList());
	}

	private List<Board> searchBoard(BoardSearchCondition boardSearchCondition, Pageable pageable) {
		return boardSearchAndFilterRepository.search(boardSearchCondition, pageable);
	}
}
