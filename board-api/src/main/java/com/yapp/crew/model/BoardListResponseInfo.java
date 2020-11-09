package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

	public static BoardListResponseInfo build(Board board, User user) {
		BoardListResponseInfo boardListResponseInfo = new BoardListResponseInfo();
		boardListResponseInfo.boardId = board.getId();
		boardListResponseInfo.hostId = board.getUser().getId();
		boardListResponseInfo.hostName = board.getUser().getNickname();
		boardListResponseInfo.title = board.getTitle();
		boardListResponseInfo.groupStatus = BoardStatusInfo.build(board.getStatus());
		boardListResponseInfo.exercise = board.getCategory().getExercise().getName();
		boardListResponseInfo.city = board.getAddress().getCity().getName();
		boardListResponseInfo.isBookMark = user.getUserBookmark().stream()
				.map(bookMark -> bookMark.getBoard().getId())
				.anyMatch(Predicate.isEqual(board.getId()));
		boardListResponseInfo.boardTime = board.showBoardTimeComparedToNow();

		return boardListResponseInfo;
	}
}
