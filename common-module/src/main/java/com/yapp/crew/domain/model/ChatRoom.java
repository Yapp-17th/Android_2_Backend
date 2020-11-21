package com.yapp.crew.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yapp.crew.domain.status.ChatRoomStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter(value = AccessLevel.PRIVATE)
	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private ChatRoomStatus status = ChatRoomStatus.ACTIVE;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "host_exited", nullable = false)
	private Boolean hostExited = false;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "guest_exited", nullable = false)
	private Boolean guestExited = false;

	@JsonBackReference
	@Setter(value = AccessLevel.PROTECTED)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User host;

	@JsonBackReference
	@Setter(value = AccessLevel.PROTECTED)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User guest;

	@JsonBackReference
	@Setter(value = AccessLevel.PRIVATE)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Board board;

	@JsonManagedReference
	@OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
	private List<Message> messages = new ArrayList<>();

	public void addMessage(Message message) {
		message.setChatRoom(this);
		this.messages.add(message);
	}

	public void exitUser(boolean isHost) {
		if (isHost) {
			setHostExited(true);
			return;
		}
		setGuestExited(true);
	}

	public boolean isUserChatRoomHost(Long userId) {
		return userId.equals(getHost().getId());
	}

	public Long countUnreadMessages(boolean isHost) {
		if (isHost) {
			return getMessages().stream()
					.filter(message -> !message.isHostRead())
					.count();
		}
		return getMessages().stream()
				.filter(message -> !message.isGuestRead())
				.count();
	}

	public Long findFirstUnreadChatMessage(boolean isHost) {
		Optional<Message> firstUnreadChatMessage;

		if (isHost) {
			firstUnreadChatMessage = getMessages().stream()
					.filter(message -> !message.isHostRead())
					.findFirst();
		} else {
			firstUnreadChatMessage = getMessages().stream()
					.filter(message -> !message.isGuestRead())
					.findFirst();
		}

		if (firstUnreadChatMessage.isPresent()) {
			return firstUnreadChatMessage.get().getMessageId();
		}
		return -1L;
	}

	public static ChatRoom buildChatRoom(User host, User guest, Board board) {
		return ChatRoom.getBuilder()
				.withHost(host)
				.withGuest(guest)
				.withBoard(board)
				.build();
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
