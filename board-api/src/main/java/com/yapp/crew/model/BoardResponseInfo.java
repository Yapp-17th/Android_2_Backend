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

  public BoardResponseInfo build(Board board, User user) {
    BoardResponseInfo boardResponseInfo = new BoardResponseInfo();
    this.boardId = board.getId();
    this.hostId = board.getUser().getId();
    this.hostName = board.getUser().getNickname();
    this.title = board.getTitle();
    this.groupStatus = board.getStatus().getName();
    this.exercise = board.getCategory().getExercise().getName();
    this.city = board.getAddress().getCity().getName();
    this.isBookMark = user.getUserBookmark().stream()
        .map(bookMark -> bookMark.getBoard().getId())
        .anyMatch(Predicate.isEqual(board.getId()));

    return boardResponseInfo;
  }
}
