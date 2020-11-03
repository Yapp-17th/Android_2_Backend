package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseInfo {

  private Long boardId;
  private Long hostId;
  private String hostName;
  private String title;
  private String groupStatus;
  private String exercise;
  private String city;
  private Boolean isBookMark;

  public static BoardResponseInfo build(Board board, User user) {
    BoardResponseInfo boardResponseInfo = new BoardResponseInfo();
    boardResponseInfo.boardId = board.getId();
    boardResponseInfo.hostId = board.getUser().getId();
    boardResponseInfo.hostName = board.getUser().getNickname();
    boardResponseInfo.title = board.getTitle();
    boardResponseInfo.groupStatus = board.getStatus().getName();
    boardResponseInfo.exercise = board.getCategory().getExercise().getName();
    boardResponseInfo.city = board.getAddress().getCity().getName();
    boardResponseInfo.isBookMark = user.getUserBookmark().stream()
        .map(bookMark -> bookMark.getBoard().getId())
        .anyMatch(Predicate.isEqual(board.getId()));

    return boardResponseInfo;
  }
}
