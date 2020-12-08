package com.yapp.crew.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardListResponseInfo {

	private long boardId;

	private long hostId;

	private String hostName;

	private String title;

	private BoardStatusInfo groupStatus;

	private String exercise;

	private String city;

	@JsonProperty(value = "isBookMark")
	private boolean isBookMark;

	private String boardTime;

	private int recruitNumber;

	private int recruitedNumber;

	public static BoardListResponseInfoBuilder getBuilder() {
		return new BoardListResponseInfoBuilder();
	}

	public static class BoardListResponseInfoBuilder {
		private long boardId = -1L;
		private long hostId = -1L;
		private String hostName = "(알수없음)";
		private String title = "";
		private BoardStatusInfo groupStatus = BoardStatusInfo.emptyBody();
		private String exercise = "";
		private String city = "";
		private boolean isBookMark = false;
		private String boardTime = "";
		private int recruitNumber = -1;
		private int recruitedNumber = -1;

		public BoardListResponseInfoBuilder withBoardId(long boardId) {
			this.boardId = boardId;
			return this;
		}

		public BoardListResponseInfoBuilder withHostId(long hostId) {
			this.hostId = hostId;
			return this;
		}

		public BoardListResponseInfoBuilder withHostName(String hostName) {
			this.hostName = hostName;
			return this;
		}

		public BoardListResponseInfoBuilder withTitle(String title) {
			this.title = title;
			return this;
		}

		public BoardListResponseInfoBuilder withGroupStatus(BoardStatusInfo groupStatus) {
			this.groupStatus = groupStatus;
			return this;
		}

		public BoardListResponseInfoBuilder withExercise(String exercise) {
			this.exercise = exercise;
			return this;
		}

		public BoardListResponseInfoBuilder withCity(String city) {
			this.city = city;
			return this;
		}

		public BoardListResponseInfoBuilder withRecruitNumber(int recruitNumber) {
			this.recruitNumber = recruitNumber;
			return this;
		}

		public BoardListResponseInfoBuilder withRecruitedNumber(int recruitedNumber) {
			this.recruitedNumber = recruitedNumber;
			return this;
		}

		public BoardListResponseInfoBuilder withIsBookMark(boolean isBookMark) {
			this.isBookMark = isBookMark;
			return this;
		}

		public BoardListResponseInfoBuilder withBoardTime(String boardTime) {
			this.boardTime = boardTime;
			return this;
		}

		public BoardListResponseInfo build() {
			BoardListResponseInfo boardListResponseInfo = new BoardListResponseInfo();
			boardListResponseInfo.setBoardId(boardId);
			boardListResponseInfo.setHostId(hostId);
			boardListResponseInfo.setHostName(hostName);
			boardListResponseInfo.setTitle(title);
			boardListResponseInfo.setGroupStatus(groupStatus);
			boardListResponseInfo.setExercise(exercise);
			boardListResponseInfo.setCity(city);
			boardListResponseInfo.setRecruitNumber(recruitNumber);
			boardListResponseInfo.setRecruitedNumber(recruitedNumber);
			boardListResponseInfo.setBookMark(isBookMark);
			boardListResponseInfo.setBoardTime(boardTime);
			return boardListResponseInfo;
		}
	}

	public static BoardListResponseInfo build(Board board, User user) {
		BoardListResponseInfoBuilder boardListResponseInfoBuilder = BoardListResponseInfo.getBuilder()
				.withBoardId(board.getId())
				.withHostId(board.getUser().getId())
				.withTitle(board.getTitle())
				.withGroupStatus(BoardStatusInfo.build(board.getStatus()))
				.withExercise(board.getCategory().getExercise().getName())
				.withCity(board.getAddress().getCity().getName())
				.withRecruitNumber(board.getRecruitCount())
				.withRecruitedNumber(board.getApprovedCount())
				.withIsBookMark(user.getUserBookmark().stream().map(bookMark -> bookMark.getBoard().getId()).anyMatch(Predicate.isEqual(board.getId())))
				.withBoardTime(board.showBoardTimeComparedToNow());

		if (user.isValidUser()) {
			return boardListResponseInfoBuilder
					.withHostName(board.getUser().getNickname())
					.build();
		}
		return boardListResponseInfoBuilder.build();
	}
}
