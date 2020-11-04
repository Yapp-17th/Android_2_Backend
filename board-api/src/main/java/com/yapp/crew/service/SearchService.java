package com.yapp.crew.service;

import com.yapp.crew.domain.model.BaseEntity;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.status.GroupStatus;
import com.yapp.crew.model.BoardResponseInfo;
import com.yapp.crew.model.BoardSearch;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchService {

  private BoardRepository boardRepository;

  @Autowired
  public SearchService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public List<BoardResponseInfo> searchBoardList(BoardSearch boardSearch) {
    List<BoardResponseInfo> boardResponseInfoPage = findByContentIsContaining(boardSearch.getKeywords())
        .stream()
        .map(board -> BoardResponseInfo.build(board, board.getUser()))
        .collect(Collectors.toList());

    log.info("결과 리스트: " + boardResponseInfoPage);

    return boardResponseInfoPage;
  }

  private List<Board> findByContentIsContaining(List<String> keywords) {
    log.info("search board 성공");
    HashSet<Board> boards = new HashSet<>(boardRepository.findByContentIsContaining(keywords.get(0)));

    for (String keyword : keywords) {
      if (boards.size() == 0) {
        return new ArrayList<Board>();
      }
      HashSet<Board> nextBoard = new HashSet<>(boardRepository.findByContentIsContaining(keyword));
      boards.retainAll(nextBoard);
    }

    return boards.stream()
        .filter(board -> board.getStatus().getCode() < GroupStatus.CANCELED.getCode())
        .sorted(Comparator.comparing(BaseEntity::getCreatedAt, Comparator.reverseOrder()))
        .collect(Collectors.toList());
  }
}
