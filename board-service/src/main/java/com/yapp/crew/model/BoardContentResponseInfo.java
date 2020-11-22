package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.BookMark;
import com.yapp.crew.domain.model.Evaluation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
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
	private LocalDateTime startsAt;

	public static BoardContentResponseInfo build(Board board, Long userId, List<Evaluation> evaluationList) {
		BoardContentResponseInfo boardContentResponseInfo = new BoardContentResponseInfo();
		boardContentResponseInfo.boardId = board.getId();
		boardContentResponseInfo.title = board.getTitle();
		boardContentResponseInfo.content = board.getContent();
		boardContentResponseInfo.place = board.getPlace();
		boardContentResponseInfo.groupStatus = BoardStatusInfo.build(board.getStatus());
		boardContentResponseInfo.exercise = board.getCategory().getExercise().getName();
		boardContentResponseInfo.city = board.getAddress().getCity().getName();
		boardContentResponseInfo.recruitNumber = board.getRecruitCount();
		boardContentResponseInfo.recruitedNumber = board.getRemainRecruitNumber();
		boardContentResponseInfo.isBookMark = board.getBookMarkUser().stream()
				.map(bookmark -> bookmark.getUser().getId())
				.anyMatch(id -> id.equals(userId));

		boardContentResponseInfo.host = HostInfo.build(board.getUser(), evaluationList);
		boardContentResponseInfo.startsAt = board.getStartsAt();

		return boardContentResponseInfo;
	}
}
