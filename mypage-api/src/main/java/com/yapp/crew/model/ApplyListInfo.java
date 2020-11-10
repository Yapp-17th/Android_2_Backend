package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.BoardStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplyListInfo {

	private long hostId;
	private long guestId;
	private String guestName;
	private boolean isHost = false;
	private ApplyStatusInfo applyStatus;
	private long chattingRoomId;

	public static ApplyListInfo build(long hostId, User guest, Board board, long chattingRoomId) {
		ApplyListInfo applyListInfo = new ApplyListInfo();
		applyListInfo.hostId = hostId;
		applyListInfo.guestId = guest.getId();
		applyListInfo.guestName = guest.getNickname();
		applyListInfo.applyStatus = ApplyStatusInfo.build(board.getAppliedUsers().stream()
				.filter(appliedUser -> appliedUser.getUser().getId().equals(guest.getId()))
				.findFirst().get().getStatus());
		applyListInfo.chattingRoomId = chattingRoomId;

		return applyListInfo;
	}
}
