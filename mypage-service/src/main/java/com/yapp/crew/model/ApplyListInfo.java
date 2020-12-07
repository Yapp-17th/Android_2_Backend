package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.status.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
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

	public static ApplyListInfoBuilder getBuilder() {
		return new ApplyListInfoBuilder();
	}

	public static class ApplyListInfoBuilder {

		private Long hostId = -1L;
		private Long guestId = -1L;
		private String guestName = "(알수없음)";
		private Boolean isHost = false;
		private ApplyStatusInfo applyStatus = ApplyStatusInfo.build(AppliedStatus.PENDING);
		private Long chattingRoomId = -1L;
		private Long boardId = -1L;

		public ApplyListInfoBuilder withHostId(Long hostId) {
			this.hostId = hostId;
			return this;
		}

		public ApplyListInfoBuilder withGuestId(Long guestId) {
			this.guestId = guestId;
			return this;
		}

		public ApplyListInfoBuilder withGuestName(String guestName) {
			this.guestName = guestName;
			return this;
		}

		public ApplyListInfoBuilder withIsHost(Boolean isHost) {
			this.isHost = isHost;
			return this;
		}

		public ApplyListInfoBuilder withApplyStatusInfo(AppliedStatus appliedStatus) {
			this.applyStatus = ApplyStatusInfo.build(appliedStatus);
			return this;
		}

		public ApplyListInfoBuilder withChattingRoomId(Long chattingRoomId) {
			this.chattingRoomId = chattingRoomId;
			return this;
		}

		public ApplyListInfoBuilder withBoardId(Long boardId) {
			this.boardId = boardId;
			return this;
		}

		public ApplyListInfo build() {
			ApplyListInfo applyListInfo = new ApplyListInfo();
			applyListInfo.setHostId(hostId);
			applyListInfo.setGuestId(guestId);
			applyListInfo.setGuestName(guestName);
			applyListInfo.setIsHost(isHost);
			applyListInfo.setApplyStatus(applyStatus);
			applyListInfo.setChattingRoomId(chattingRoomId);
			applyListInfo.setBoardId(boardId);

			return applyListInfo;
		}

	}

	public static ApplyListInfo build(long hostId, User guest, Board board, ChatRoom chatRoom) {
		Long chattingRoomId = chatRoom != null ? chatRoom.getId() : 0L;

		ApplyListInfoBuilder applyListInfoBuilder = ApplyListInfo.getBuilder()
				.withHostId(hostId)
				.withApplyStatusInfo(board.getAppliedUsers().stream()
						.filter(appliedUser -> appliedUser.getUser().getId().equals(guest.getId()))
						.findFirst().get().getStatus())
				.withChattingRoomId(chattingRoomId)
				.withBoardId(board.getId());

		if (guest.getStatus() == UserStatus.ACTIVE || guest.getStatus() == UserStatus.SUSPENDED) {
			return applyListInfoBuilder
					.withGuestId(guest.getId())
					.withGuestName(guest.getNickname())
					.build();
		}

		return applyListInfoBuilder.build();
	}
}
