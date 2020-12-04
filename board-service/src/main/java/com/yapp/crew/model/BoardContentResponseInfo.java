package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.Tag;
import com.yapp.crew.domain.type.UserTag;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardContentResponseInfo {

	private Long boardId;
	private String title;
	private String content;
	private String place;
	private BoardStatusInfo groupStatus;
	private String exercise;
	private String city;
	private Integer recruitNumber;
	private Integer recruitedNumber;
	private Boolean isBookMark;
	private HostInfo host;
	private String boardTime;
	private Date startsAt;
	private String userTag;

	public static BoardContentResponseInfoBuilder getBuilder() {
		return new BoardContentResponseInfoBuilder();
	}

	public static class BoardContentResponseInfoBuilder {

		private Long boardId = -1L;
		private String title = "";
		private String content = "";
		private String place = "";
		private BoardStatusInfo groupStatus = BoardStatusInfo.emptyBody();
		private String exercise = "";
		private String city = "";
		private Integer recruitNumber = -1;
		private Integer recruitedNumber = -1;
		private Boolean isBookMark = false;
		private HostInfo host = HostInfo.emptyBody();
		private String boardTime = "";
		private Date startsAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
		private String userTag = "";

		public BoardContentResponseInfoBuilder withBoardId(Long boardId) {
			this.boardId = boardId;
			return this;
		}

		public BoardContentResponseInfoBuilder withTitle(String title) {
			this.title = title;
			return this;
		}

		public BoardContentResponseInfoBuilder withContent(String content) {
			this.content = content;
			return this;
		}

		public BoardContentResponseInfoBuilder withPlace(String place) {
			this.place = place;
			return this;
		}

		public BoardContentResponseInfoBuilder withGroupStatus(BoardStatusInfo groupStatus) {
			this.groupStatus = groupStatus;
			return this;
		}

		public BoardContentResponseInfoBuilder withExcercise(String excercise) {
			this.exercise = excercise;
			return this;
		}

		public BoardContentResponseInfoBuilder withCity(String city) {
			this.city = city;
			return this;
		}

		public BoardContentResponseInfoBuilder withRecruitNumber(Integer recruitNumber) {
			this.recruitNumber = recruitNumber;
			return this;
		}

		public BoardContentResponseInfoBuilder withRecruitedNumber(Integer recruitedNumber) {
			this.recruitedNumber = recruitedNumber;
			return this;
		}

		public BoardContentResponseInfoBuilder withIsBookMark(Boolean isBookMark) {
			this.isBookMark = isBookMark;
			return this;
		}

		public BoardContentResponseInfoBuilder withHost(HostInfo host) {
			this.host = host;
			return this;
		}

		public BoardContentResponseInfoBuilder withBoardTime(String boardTime) {
			this.boardTime = boardTime;
			return this;
		}

		public BoardContentResponseInfoBuilder withStartsAt(LocalDateTime startsAt) {
			this.startsAt = Date.from(startsAt.atZone(ZoneId.systemDefault()).toInstant());
			return this;
		}

		public BoardContentResponseInfoBuilder withUserTag(UserTag userTag) {
			this.userTag = userTag.getName();
			return this;
		}

		public BoardContentResponseInfo build() {
			BoardContentResponseInfo boardContentResponseInfo = new BoardContentResponseInfo();
			boardContentResponseInfo.setBoardId(boardId);
			boardContentResponseInfo.setTitle(title);
			boardContentResponseInfo.setContent(content);
			boardContentResponseInfo.setPlace(place);
			boardContentResponseInfo.setGroupStatus(groupStatus);
			boardContentResponseInfo.setExercise(exercise);
			boardContentResponseInfo.setCity(city);
			boardContentResponseInfo.setRecruitNumber(recruitNumber);
			boardContentResponseInfo.setRecruitedNumber(recruitedNumber);
			boardContentResponseInfo.setIsBookMark(isBookMark);
			boardContentResponseInfo.setHost(host);
			boardContentResponseInfo.setBoardTime(boardTime);
			boardContentResponseInfo.setStartsAt(startsAt);
			boardContentResponseInfo.setUserTag(userTag);
			return boardContentResponseInfo;
		}
	}

	public static BoardContentResponseInfo build(Board board, Long userId, List<Evaluation> evaluationList) {
		return BoardContentResponseInfo.getBuilder()
				.withBoardId(board.getId())
				.withTitle(board.getTitle())
				.withContent(board.getContent())
				.withPlace(board.getPlace())
				.withGroupStatus(BoardStatusInfo.build(board.getStatus()))
				.withExcercise(board.getCategory().getExercise().getName())
				.withCity(board.getAddress().getCity().getName())
				.withRecruitNumber(board.getRecruitCount())
				.withRecruitedNumber(board.getApprovedCount())
				.withIsBookMark(board.getBookMarkUser().stream().map(bookmark -> bookmark.getUser().getId()).anyMatch(id -> id.equals(userId)))
				.withHost(HostInfo.build(board.getUser(), evaluationList))
				.withBoardTime(board.showBoardTimeComparedToNow())
				.withStartsAt(board.getStartsAt())
				.withUserTag(board.getTag().getName())
				.build();
	}
}
