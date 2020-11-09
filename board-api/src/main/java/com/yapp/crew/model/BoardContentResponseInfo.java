package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.BookMark;
import com.yapp.crew.domain.model.Evaluation;
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

	public static BoardContentResponseInfo build(Board board, List<Evaluation> evaluationList) {
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
		boardContentResponseInfo.isBookMark = board.getUser().getUserBookmark().stream().map(BookMark::getBoard).collect(Collectors.toSet()).contains(board);
		boardContentResponseInfo.host = HostInfo.build(board.getUser(), evaluationList);

		return boardContentResponseInfo;
	}
}
