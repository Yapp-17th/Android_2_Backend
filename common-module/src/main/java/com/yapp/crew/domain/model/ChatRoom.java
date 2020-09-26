package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.ChatRoomStatus;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.ORDINAL)
  private ChatRoomStatus status = ChatRoomStatus.ACTIVE;

  @ManyToOne(fetch = FetchType.LAZY)
  private User host;

  @ManyToOne(fetch = FetchType.LAZY)
  private User guest;

  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;

  @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
  private List<Message> messages = new ArrayList<>();

  protected ChatRoom() {

  }
}
