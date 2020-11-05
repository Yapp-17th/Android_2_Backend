package com.yapp.crew.service;

import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.BaseEntity;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.HiddenBoard;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.GroupStatus;
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.model.BoardSearch;
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

@Slf4j
@Service
public class SearchService {

  private BoardRepository boardRepository;
  private UserRepository userRepository;

  @Autowired
  public SearchService(BoardRepository boardRepository, UserRepository userRepository) {
    this.boardRepository = boardRepository;
    this.userRepository = userRepository;
  }

  public List<BoardListResponseInfo> searchBoardList(BoardSearch boardSearch) {
    User user = findUserById(boardSearch.getUserId())
        .orElseThrow(() -> new UserNotFoundException("user not found"));

    List<BoardListResponseInfo> boardListResponseInfoPage = findByContentIsContaining(boardSearch.getKeywords(), user)
        .stream()
        .map(board -> BoardListResponseInfo.build(board, board.getUser()))
        .collect(Collectors.toList());

    log.info("결과 리스트: " + boardListResponseInfoPage);

    return boardListResponseInfoPage;
  }

  private List<Board> findByContentIsContaining(List<String> keywords, User user) {
    log.info("search board 성공");
    Set<Board> hiddenBoards = user.getUserHiddenBoard().stream().map(HiddenBoard::getBoard).collect(Collectors.toSet());
    HashSet<Board> boards = new HashSet<>(boardRepository.findByContentIsContaining(keywords.get(0)));

    for (String keyword : keywords) {
      if (boards.size() == 0) {
        return new ArrayList<>();
      }
      HashSet<Board> nextBoard = new HashSet<>(boardRepository.findByContentIsContaining(keyword));
      boards.retainAll(nextBoard);
    }

    return boards.stream()
        .filter(board -> board.getStatus().getCode() < GroupStatus.CANCELED.getCode())
        .filter(board -> !hiddenBoards.contains(board))
        .sorted(Comparator.comparing(BaseEntity::getCreatedAt, Comparator.reverseOrder()))
        .collect(Collectors.toList());
  }

  private Optional<User> findUserById(Long userId) {
    log.info("user 가져오기 성공");
    return userRepository.findUserById(userId);
  }
}
