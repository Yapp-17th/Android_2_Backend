package com.yapp.crew.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yapp.crew.domain.type.MessageType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
public class Message extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false, length = 500)
  private String content;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private MessageType type;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(name = "is_host_read", nullable = false)
  private boolean isHostRead = false;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "is_guest_read", nullable = false)
	private boolean isGuestRead = false;

	@Embedded
	@Setter(value = AccessLevel.PRIVATE)
	private ProfileMessage profileMessage;

  @JsonBackReference
  @Setter(value = AccessLevel.PRIVATE)
  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private User sender;

  @JsonBackReference
  @Setter(value = AccessLevel.PROTECTED)
  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private ChatRoom chatRoom;

  public void readMessage(boolean isHost) {
  	if (isHost) {
			setHostRead(true);
  		return;
		}
  	setGuestRead(true);
	}

	public static Message buildChatMessage(String content, MessageType type, User sender, ChatRoom chatRoom) {
		return Message.getBuilder()
						.withContent(content)
						.withType(type)
						.withIsHostRead(false)
						.withIsGuestRead(false)
						.withSender(sender)
						.withChatRoom(chatRoom)
						.build();
	}

	public static Message buildProfileMessage(String content, User sender, ChatRoom chatRoom, List<Evaluation> evaluations) {
		return Message.getBuilder()
						.withContent(content)
						.withType(MessageType.PROFILE)
						.withIsHostRead(false)
						.withIsGuestRead(false)
						.withSender(sender)
						.withChatRoom(chatRoom)
						.withLikes(sender.calculateLikes(evaluations))
						.withDislikes(sender.calculateDislikes(evaluations))
						.build();
	}

  public static MessageBuilder getBuilder() {
    return new MessageBuilder();
  }

  public static class MessageBuilder {
    private String content;
    private MessageType type;
    private boolean isHostRead;
    private boolean isGuestRead;
    private Long likes;
    private Long dislikes;
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

    public MessageBuilder withIsHostRead(boolean isHostRead) {
    	this.isHostRead = isHostRead;
    	return this;
		}

		public MessageBuilder withIsGuestRead(boolean isGuestRead) {
			this.isGuestRead = isGuestRead;
			return this;
		}

		public MessageBuilder withLikes(Long likes) {
    	this.likes = likes;
    	return this;
		}

		public MessageBuilder withDislikes(Long dislikes) {
    	this.dislikes = dislikes;
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
      message.setHostRead(isHostRead);
      message.setGuestRead(isGuestRead);
      message.setProfileMessage(new ProfileMessage(likes, dislikes));
      message.setSender(sender);
      message.setChatRoom(chatRoom);
      return message;
    }
  }
}
