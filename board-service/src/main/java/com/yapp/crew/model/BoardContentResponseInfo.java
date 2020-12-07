package com.yapp.crew.model;

import static java.time.LocalDateTime.now;

import com.yapp.crew.domain.model.Address;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.Tag;
import java.time.LocalDateTime;
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
	private CategoryCode exercise;
	private CityCode city;
	private Integer recruitNumber;
	private Integer recruitedNumber;
	private Boolean isBookMark;
	private HostInfo host;
	private String boardTime;
	private LocalDateTime startsAt;
	private TagCode userTag;

	public static BoardContentResponseInfoBuilder getBuilder() {
		return new BoardContentResponseInfoBuilder();
	}

	public static class BoardContentResponseInfoBuilder {

		private Long boardId = -1L;
		private String title = "";
		private String content = "";
		private String place = "";
		private BoardStatusInfo groupStatus = BoardStatusInfo.emptyBody();
		private CategoryCode exercise = CategoryCode.build(-1L, "");
		private CityCode city = CityCode.build(-1L, "");
		private TagCode userTag = TagCode.build(-1L, "");
		private Integer recruitNumber = 0;
		private Integer recruitedNumber = 0;
		private Boolean isBookMark = false;
		private HostInfo host = HostInfo.emptyBody();
		private String boardTime = "";
		private LocalDateTime startsAt = now();

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

		public BoardContentResponseInfoBuilder withExcercise(Category category) {
			this.exercise = CategoryCode.build(category);
			return this;
		}

		public BoardContentResponseInfoBuilder withCity(Address address) {
			this.city = CityCode.build(address);
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
			this.startsAt = startsAt;
			return this;
		}

		public BoardContentResponseInfoBuilder withUserTag(Tag tag) {
			this.userTag = TagCode.build(tag);
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
				.withExcercise(board.getCategory())
				.withCity(board.getAddress())
				.withRecruitNumber(board.getRecruitCount())
				.withRecruitedNumber(board.getApprovedCount())
				.withIsBookMark(board.getBookMarkUser().stream().map(bookmark -> bookmark.getUser().getId()).anyMatch(id -> id.equals(userId)))
				.withHost(HostInfo.build(board.getUser(), evaluationList))
				.withBoardTime(board.showBoardTimeComparedToNow())
				.withStartsAt(board.getStartsAt())
				.withUserTag(board.getTag())
				.build();
	}
}
