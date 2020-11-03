package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BoardContentResponseInfo {

  private Long boardId;
  private String title;
  private String groupStatus;
  private String exercise;
  private String city;
  private int recruitNumber;
  private int recruitedNumber;
  private Boolean isBookMark;
  private HostInfo host;

  public static BoardContentResponseInfo build(Board board, List<Evaluation> evaluationList) {
    BoardContentResponseInfo boardContentResponseInfo = new BoardContentResponseInfo();
    boardContentResponseInfo.boardId = board.getId();
    boardContentResponseInfo.title = board.getTitle();
    boardContentResponseInfo.groupStatus = board.getStatus().getName();
    boardContentResponseInfo.exercise = board.getCategory().getExercise().getName();
    boardContentResponseInfo.city = board.getAddress().getCity().getName();
    boardContentResponseInfo.recruitNumber = board.getRecruitCount();
    boardContentResponseInfo.recruitedNumber = board.getEvaluations().size();
    boardContentResponseInfo.host = HostInfo.build(board.getUser(), evaluationList);

    return boardContentResponseInfo;
  }
}
