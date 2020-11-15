package com.yapp.crew.service;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.repository.BoardRepository;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BoardBatchService {

	private BoardRepository boardRepository;
	private EntityManager entityManager;

	@Autowired
	public BoardBatchService(BoardRepository boardRepository, EntityManager entityManager) {
		this.boardRepository = boardRepository;
		this.entityManager = entityManager;
	}

	void updateBoardFinishedAll(List<Board> boardList) {
		boardList.forEach(Board::finishBoard);
		entityManager.flush();
	}
}
