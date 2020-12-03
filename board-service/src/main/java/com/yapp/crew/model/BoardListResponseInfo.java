package com.yapp.crew.model;

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

	private Long boardId;
	private Long hostId;
	private String hostName;
	private String title;
	private BoardStatusInfo groupStatus;
	private String exercise;
	private String city;
	private Boolean isBookMark;
	private String boardTime;
	private Integer recruitNumber;
	private Integer recruitedNumber;

	public static BoardListResponseInfoBuilder getBuilder() {
		return new BoardListResponseInfoBuilder();
	}

	public static class BoardListResponseInfoBuilder {
		private Long boardId = -1L;
		private Long hostId = -1L;
		private String hostName = "";
		private String title = "";
		private BoardStatusInfo groupStatus = BoardStatusInfo.emptyBody();
		private String exercise = "";
		private String city = "";
		private Boolean isBookMark = false;
		private String boardTime = "";
		private Integer recruitNumber = -1;
		private Integer recruitedNumber = -1;

		public BoardListResponseInfoBuilder withBoardId(Long boardId) {
			this.boardId = boardId;
			return this;
		}

		public BoardListResponseInfoBuilder withHostId(Long hostId) {
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

		public BoardListResponseInfoBuilder withRecruitNumber(Integer recruitNumber) {
			this.recruitNumber = recruitNumber;
			return this;
		}

		public BoardListResponseInfoBuilder withRecruitedNumber(Integer recruitedNumber) {
			this.recruitedNumber = recruitedNumber;
			return this;
		}

		public BoardListResponseInfoBuilder withIsBookMark(Boolean isBookMark) {
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
			boardListResponseInfo.setIsBookMark(isBookMark);
			boardListResponseInfo.setBoardTime(boardTime);
			return boardListResponseInfo;
		}
	}

	public static BoardListResponseInfo build(Board board, User user) {
		return BoardListResponseInfo.getBuilder()
				.withBoardId(board.getId())
				.withHostId(board.getUser().getId())
				.withHostName(board.getUser().getNickname())
				.withTitle(board.getTitle())
				.withGroupStatus(BoardStatusInfo.build(board.getStatus()))
				.withExercise(board.getCategory().getExercise().getName())
				.withCity(board.getAddress().getCity().getName())
				.withRecruitNumber(board.getRecruitCount())
				.withRecruitedNumber(board.getRemainRecruitNumber())
				.withIsBookMark(user.getUserBookmark().stream().map(bookMark -> bookMark.getBoard().getId()).anyMatch(Predicate.isEqual(board.getId())))
				.withBoardTime(board.showBoardTimeComparedToNow())
				.build();
	}
}
