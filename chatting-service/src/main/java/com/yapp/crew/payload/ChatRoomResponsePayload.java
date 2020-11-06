package com.yapp.crew.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.status.ChatRoomStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponsePayload {

	private Long id;

	private Long hostId;

	private Long guestId;

	private Long boardId;

	@JsonInclude(value = Include.NON_NULL)
	private String opponentNickname;

	private ChatRoomStatus status;

	private LocalDateTime createdAt;

	@JsonInclude(value = Include.NON_NULL)
	private MessageResponsePayload lastMessage;

	@JsonInclude(value = Include.NON_NULL)
	private long unreadMessages;

	public static ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom) {
		return ChatRoomResponsePayload.builder()
						.id(chatRoom.getId())
						.hostId(chatRoom.getHost().getId())
						.guestId(chatRoom.getGuest().getId())
						.boardId(chatRoom.getBoard().getId())
						.status(chatRoom.getStatus())
						.createdAt(chatRoom.getCreatedAt())
						.build();
	}

	public static ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom, MessageResponsePayload lastMessage, Long unreadMessages) {
		return ChatRoomResponsePayload.builder()
						.id(chatRoom.getId())
						.hostId(chatRoom.getHost().getId())
						.guestId(chatRoom.getGuest().getId())
						.boardId(chatRoom.getBoard().getId())
						.status(chatRoom.getStatus())
						.lastMessage(lastMessage)
						.unreadMessages(unreadMessages)
						.createdAt(chatRoom.getCreatedAt())
						.build();
	}

	public static ChatRoomResponsePayload buildChatRoomResponsePayload(ChatRoom chatRoom, MessageResponsePayload lastMessage, Long unreadMessages, String opponentNickname) {
		return ChatRoomResponsePayload.builder()
						.id(chatRoom.getId())
						.hostId(chatRoom.getHost().getId())
						.guestId(chatRoom.getGuest().getId())
						.boardId(chatRoom.getBoard().getId())
						.status(chatRoom.getStatus())
						.lastMessage(lastMessage)
						.unreadMessages(unreadMessages)
						.opponentNickname(opponentNickname)
						.createdAt(chatRoom.getCreatedAt())
						.build();
	}

	public static List<ChatRoomResponsePayload> buildChatRoomResponsePayload(List<ChatRoom> chatRooms, Long userId) {
		return chatRooms.stream()
						.map(chatRoom -> {
							List<Message> messages = chatRoom.getMessages();

							boolean isHost = chatRoom.isSenderChatRoomHost(userId);
							Long unreadMessages = chatRoom.countUnreadMessages(isHost);
							String opponentNickname;
							if (isHost) {
								opponentNickname = chatRoom.getGuest().getNickname();
							}
							else {
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
