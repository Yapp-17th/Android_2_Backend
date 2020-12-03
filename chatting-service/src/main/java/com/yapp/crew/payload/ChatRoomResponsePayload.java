package com.yapp.crew.payload;

import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
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

	private Long id;

	private Long hostId;

	private Long guestId;

	private Long boardId;

	private String opponentNickname;

	private String status;

	private LocalDateTime createdAt;

	private MessageResponsePayload lastMessage;

	private Long unreadMessages;

	public static ChatRoomResponseBuilder getBuilder() {
		return new ChatRoomResponseBuilder();
	}

	public static class ChatRoomResponseBuilder {
		private Long id = -1L;
		private Long hostId = -1L;
		private Long guestId = -1L;
		private Long boardId = -1L;
		private String opponentNickname = "";
		private String status = "";
		private LocalDateTime createdAt = LocalDateTime.now();
		private MessageResponsePayload lastMessage = MessageResponsePayload.emptyBody();
		private Long unreadMessages = -1L;

		public ChatRoomResponseBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public ChatRoomResponseBuilder withHostId(Long hostId) {
			this.hostId = hostId;
			return this;
		}

		public ChatRoomResponseBuilder withGuestId(Long guestId) {
			this.guestId = guestId;
			return this;
		}

		public ChatRoomResponseBuilder withBoardId(Long boardId) {
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

		public ChatRoomResponseBuilder withUnreadMessages(Long unreadMessages) {
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

	public static ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom) {
		return ChatRoomResponsePayload.getBuilder()
				.withId(chatRoom.getId())
				.withHostId(chatRoom.getHost().getId())
				.withGuestId(chatRoom.getGuest().getId())
				.withBoardId(chatRoom.getBoard().getId())
				.withStatus(chatRoom.getStatus())
				.withCreatedAt(chatRoom.getCreatedAt())
				.build();
	}

	public static ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom, MessageResponsePayload lastMessage, Long unreadMessages) {
		return ChatRoomResponsePayload.getBuilder()
				.withId(chatRoom.getId())
				.withHostId(chatRoom.getHost().getId())
				.withGuestId(chatRoom.getGuest().getId())
				.withBoardId(chatRoom.getBoard().getId())
				.withStatus(chatRoom.getStatus())
				.withLastMessage(lastMessage)
				.withUnreadMessages(unreadMessages)
				.withCreatedAt(chatRoom.getCreatedAt())
				.build();
	}

	public static ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom, MessageResponsePayload lastMessage, Long unreadMessages, String opponentNickname) {
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

	public static List<ChatRoomResponsePayload> buildChatRoomResponsePayload(List<ChatRoom> chatRooms, Long userId) {
		return chatRooms.stream()
				.map(chatRoom -> {
					List<Message> messages = chatRoom.getMessages();

					boolean isHost = chatRoom.isUserChatRoomHost(userId);
					Long unreadMessages = chatRoom.countUnreadMessages(isHost);
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
