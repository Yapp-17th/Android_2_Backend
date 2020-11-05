package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardListResponseInfo {

  private Long boardId;
  private Long hostId;
  private String hostName;
  private String title;
  private String groupStatus;
  private String exercise;
  private String city;
  private Boolean isBookMark;

  public static BoardListResponseInfo build(Board board, User user) {
    BoardListResponseInfo boardListResponseInfo = new BoardListResponseInfo();
    boardListResponseInfo.boardId = board.getId();
    boardListResponseInfo.hostId = board.getUser().getId();
    boardListResponseInfo.hostName = board.getUser().getNickname();
    boardListResponseInfo.title = board.getTitle();
    boardListResponseInfo.groupStatus = board.getStatus().getName();
    boardListResponseInfo.exercise = board.getCategory().getExercise().getName();
    boardListResponseInfo.city = board.getAddress().getCity().getName();
    boardListResponseInfo.isBookMark = user.getUserBookmark().stream()
        .map(bookMark -> bookMark.getBoard().getId())
        .anyMatch(Predicate.isEqual(board.getId()));

    return boardListResponseInfo;
  }
}