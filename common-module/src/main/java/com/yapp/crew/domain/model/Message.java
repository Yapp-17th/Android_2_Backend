package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Message extends BaseEntity {

  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  private String content;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  @Enumerated(value = EnumType.ORDINAL)
  private MessageType type;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Column(name = "is_read", nullable = false)
  private boolean isRead = false;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Transient
  private User sender;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private ChatRoom chatRoom;

  protected Message() {

  }

  public static MessageBuilder getBuilder() {
    return new MessageBuilder();
  }

  public static class MessageBuilder {
    private String content;
    private MessageType type;
    private User sender;
    private ChatRoom chatRoom;

    public MessageBuilder withContent(String content) {
      this.content = content;
      return this;
    }

    public MessageBuilder withType(MessageType type) {
      this.type = type;
      return this;
    }

    public MessageBuilder withSender(User sender) {
      this.sender = sender;
      return this;
    }

    public MessageBuilder withChatRoom(ChatRoom chatRoom) {
      this.chatRoom = chatRoom;
      return this;
    }

    public Message build() {
      Message message = new Message();
      message.setContent(content);
      message.setType(type);
      message.setSender(sender);
      message.setChatRoom(chatRoom);
      return message;
    }
  }
}
