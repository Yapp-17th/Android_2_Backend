package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.User;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplyListInfo {

	private Long hostId;
	private Long guestId;
	private String guestName;
	private Boolean isHost = false;
	private ApplyStatusInfo applyStatus;
	private Long chattingRoomId;
	private Long boardId;

	public static ApplyListInfo build(long hostId, User guest, Board board, Optional<ChatRoom> chatRoom) {
		ApplyListInfo applyListInfo = new ApplyListInfo();
		applyListInfo.hostId = hostId;
		applyListInfo.guestId = guest.getId();
		applyListInfo.guestName = guest.getNickname();
		applyListInfo.applyStatus = ApplyStatusInfo.build(board.getAppliedUsers().stream()
				.filter(appliedUser -> appliedUser.getUser().getId().equals(guest.getId()))
				.findFirst().get().getStatus());
		applyListInfo.chattingRoomId = chatRoom.isPresent() ? chatRoom.get().getId() : 0L;
		applyListInfo.boardId = board.getId();

		return applyListInfo;
	}
}
