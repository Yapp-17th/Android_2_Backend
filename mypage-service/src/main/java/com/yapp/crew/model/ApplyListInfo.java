package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.AppliedStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ApplyListInfo {

	private long hostId;
	private long guestId;
	private String guestName;
	private boolean isHost;
	private ApplyStatusInfo applyStatus;
	private long chattingRoomId;
	private long boardId;

	public static ApplyListInfoBuilder getBuilder() {
		return new ApplyListInfoBuilder();
	}

	public static class ApplyListInfoBuilder {
		private long hostId = -1L;
		private long guestId = -1L;
		private String guestName = "(알수없음)";
		private boolean isHost = false;
		private ApplyStatusInfo applyStatus = ApplyStatusInfo.build(AppliedStatus.PENDING);
		private long chattingRoomId = -1L;
		private long boardId = -1L;

		public ApplyListInfoBuilder withHostId(long hostId) {
			this.hostId = hostId;
			return this;
		}

		public ApplyListInfoBuilder withGuestId(long guestId) {
			this.guestId = guestId;
			return this;
		}

		public ApplyListInfoBuilder withGuestName(String guestName) {
			this.guestName = guestName;
			return this;
		}

		public ApplyListInfoBuilder withIsHost(boolean isHost) {
			this.isHost = isHost;
			return this;
		}

		public ApplyListInfoBuilder withApplyStatusInfo(AppliedStatus appliedStatus) {
			this.applyStatus = ApplyStatusInfo.build(appliedStatus);
			return this;
		}

		public ApplyListInfoBuilder withChattingRoomId(long chattingRoomId) {
			this.chattingRoomId = chattingRoomId;
			return this;
		}

		public ApplyListInfoBuilder withBoardId(long boardId) {
			this.boardId = boardId;
			return this;
		}

		public ApplyListInfo build() {
			ApplyListInfo applyListInfo = new ApplyListInfo();
			applyListInfo.setHostId(hostId);
			applyListInfo.setGuestId(guestId);
			applyListInfo.setGuestName(guestName);
			applyListInfo.setHost(isHost);
			applyListInfo.setApplyStatus(applyStatus);
			applyListInfo.setChattingRoomId(chattingRoomId);
			applyListInfo.setBoardId(boardId);

			return applyListInfo;
		}

	}

	public static ApplyListInfo build(long hostId, User guest, Board board, ChatRoom chatRoom) {
		long chattingRoomId = chatRoom != null ? chatRoom.getId() : 0L;

		ApplyListInfoBuilder applyListInfoBuilder = ApplyListInfo.getBuilder()
				.withHostId(hostId)
				.withApplyStatusInfo(board.getAppliedUsers().stream()
						.filter(appliedUser -> appliedUser.getUser().getId() == guest.getId())
						.findFirst().get().getStatus())
				.withChattingRoomId(chattingRoomId)
				.withBoardId(board.getId());

		if (guest.isValidUser()) {
			return applyListInfoBuilder
					.withGuestId(guest.getId())
					.withGuestName(guest.getNickname())
					.build();
		}
		return applyListInfoBuilder.build();
	}
}
