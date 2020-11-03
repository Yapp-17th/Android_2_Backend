package com.yapp.crew.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HiddenBoard extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;

  public static HiddenBoardBuilder getBuilder() {
    return new HiddenBoardBuilder();
  }

  public static class HiddenBoardBuilder {

    private User user;
    private Board board;

    public HiddenBoardBuilder withUser(User user) {
      this.user = user;
      return this;
    }

    public HiddenBoardBuilder withBoard(Board board) {
      this.board = board;
      return this;
    }

    public HiddenBoard build() {
      HiddenBoard hiddenBoard = new HiddenBoard();
      hiddenBoard.user = user;
      hiddenBoard.board = board;
      return hiddenBoard;
    }
  }
}
