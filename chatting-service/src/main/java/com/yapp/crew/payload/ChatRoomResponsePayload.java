package com.yapp.crew.payload;

import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.ChatRoomStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponsePayload {

	private long id;

	private long hostId;

	private long guestId;

	private long boardId;

	private String opponentNickname;

	private String status;

	private LocalDateTime createdAt;

	private MessageResponsePayload lastMessage;

	private long unreadMessages;

	public static ChatRoomResponseBuilder getBuilder() {
		return new ChatRoomResponseBuilder();
	}

	public static class ChatRoomResponseBuilder {
		private long id = -1L;
		private long hostId = -1L;
		private long guestId = -1L;
		private long boardId = -1L;
		private String opponentNickname = "";
		private String status = "";
		private LocalDateTime createdAt = LocalDateTime.now();
		private MessageResponsePayload lastMessage = MessageResponsePayload.emptyBody();
		private long unreadMessages = -1L;

		public ChatRoomResponseBuilder withId(long id) {
			this.id = id;
			return this;
		}

		public ChatRoomResponseBuilder withHostId(long hostId) {
			this.hostId = hostId;
			return this;
		}

		public ChatRoomResponseBuilder withGuestId(long guestId) {
			this.guestId = guestId;
			return this;
		}

		public ChatRoomResponseBuilder withBoardId(long boardId) {
			this.boardId = boardId;
			return this;
		}

		public ChatRoomResponseBuilder withOpponentNickname(String opponentNickname) {
			this.opponentNickname = opponentNickname;
			return this;
		}

		public ChatRoomResponseBuilder withStatus(ChatRoomStatus status) {
			this.status = status.name();
			return this;
		}

		public ChatRoomResponseBuilder withCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public ChatRoomResponseBuilder withLastMessage(MessageResponsePayload lastMessage) {
			this.lastMessage = lastMessage;
			return this;
		}

		public ChatRoomResponseBuilder withUnreadMessages(long unreadMessages) {
			this.unreadMessages = unreadMessages;
			return this;
		}

		public ChatRoomResponsePayload build() {
			ChatRoomResponsePayload payload = new ChatRoomResponsePayload();
			payload.setId(id);
			payload.setHostId(hostId);
			payload.setGuestId(guestId);
			payload.setBoardId(boardId);
			payload.setOpponentNickname(opponentNickname);
			payload.setStatus(status);
			payload.setCreatedAt(createdAt);
			payload.setLastMessage(lastMessage);
			payload.setUnreadMessages(unreadMessages);
			return payload;
		}
	}

	public static ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom, User user) {
		return ChatRoomResponsePayload.getBuilder()
				.withId(chatRoom.getId())
				.withHostId(chatRoom.getHost().getId())
				.withGuestId(chatRoom.getGuest().getId())
				.withBoardId(chatRoom.getBoard().getId())
				.withOpponentNickname(user.getNickname())
				.withStatus(chatRoom.getStatus())
				.withCreatedAt(chatRoom.getCreatedAt())
				.build();
	}

	public static ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom, MessageResponsePayload lastMessage, long unreadMessages, String opponentNickname) {
		return ChatRoomResponsePayload.getBuilder()
				.withId(chatRoom.getId())
				.withHostId(chatRoom.getHost().getId())
				.withGuestId(chatRoom.getGuest().getId())
				.withBoardId(chatRoom.getBoard().getId())
				.withStatus(chatRoom.getStatus())
				.withLastMessage(lastMessage)
				.withUnreadMessages(unreadMessages)
				.withOpponentNickname(opponentNickname)
				.withCreatedAt(chatRoom.getCreatedAt())
				.build();
	}

	public static List<ChatRoomResponsePayload> buildChatRoomResponsePayload(List<ChatRoom> chatRooms, long userId) {
		return chatRooms.stream()
				.map(chatRoom -> {
					List<Message> messages = chatRoom.getMessages();

					boolean isHost = chatRoom.isUserChatRoomHost(userId);
					long unreadMessages = chatRoom.countUnreadMessages(isHost);
					String opponentNickname;
					if (isHost) {
						opponentNickname = chatRoom.getGuest().getNickname();
					} else {
						opponentNickname = chatRoom.getHost().getNickname();
					}

					Message lastMessage = null;
					if (messages.size() > 0) {
						lastMessage = messages.get(messages.size() - 1);
					}

					MessageResponsePayload messagePayload = null;
					if (lastMessage != null) {
						messagePayload = MessageResponsePayload.buildChatMessageResponsePayload(lastMessage);
					}
					return ChatRoomResponsePayload.buildChatRoomResponsePayload(chatRoom, messagePayload, unreadMessages, opponentNickname);
				})
				.collect(Collectors.toList());
	}
}
