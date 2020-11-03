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
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @Setter(value = AccessLevel.PRIVATE)
  private User user;

  @ManyToOne
  @JoinColumn(name = "board_id")
  @Setter(value = AccessLevel.PRIVATE)
  private Board board;

  public static BookMarkBuilder getBuilder() {
    return new BookMarkBuilder();
  }

  public static class BookMarkBuilder {

    private User user;
    private Board board;

    public BookMarkBuilder withUser(User user) {
      this.user = user;
      return this;
    }

    public BookMarkBuilder withBoard(Board board) {
      this.board = board;
      return this;
    }

    public BookMark build() {
      BookMark bookMark = new BookMark();
      bookMark.setUser(user);
      bookMark.setBoard(board);

      return bookMark;
    }
  }
}
