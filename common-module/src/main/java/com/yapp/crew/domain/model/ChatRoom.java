package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.ChatRoomStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChatRoom extends BaseEntity {

  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Enumerated(value = EnumType.ORDINAL)
  private ChatRoomStatus status = ChatRoomStatus.ACTIVE;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private User host;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private User guest;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;

  @Getter
  @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
  private List<Message> messages = new ArrayList<>();

  protected ChatRoom() {

  }

  public static ChatRoomBuilder getBuilder() {
    return new ChatRoomBuilder();
  }

  public static class ChatRoomBuilder {
    private User host;
    private User guest;
    private Board board;

    public ChatRoomBuilder withHost(User host) {
      this.host = host;
      return this;
    }

    public ChatRoomBuilder withGuest(User guest) {
      this.guest = guest;
      return this;
    }

    public ChatRoomBuilder withBoard(Board board) {
      this.board = board;
      return this;
    }

    public ChatRoom build() {
      ChatRoom chatRoom = new ChatRoom();
      chatRoom.setHost(host);
      chatRoom.setGuest(guest);
      chatRoom.setBoard(board);
      return chatRoom;
    }
  }
}
