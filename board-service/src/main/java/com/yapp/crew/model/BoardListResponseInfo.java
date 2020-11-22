package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

	public static BoardListResponseInfo build(Board board, Long userId) {
		BoardListResponseInfo boardListResponseInfo = new BoardListResponseInfo();
		boardListResponseInfo.boardId = board.getId();
		boardListResponseInfo.hostId = board.getUser().getId();
		boardListResponseInfo.hostName = board.getUser().getNickname();
		boardListResponseInfo.title = board.getTitle();
		boardListResponseInfo.groupStatus = BoardStatusInfo.build(board.getStatus());
		boardListResponseInfo.exercise = board.getCategory().getExercise().getName();
		boardListResponseInfo.city = board.getAddress().getCity().getName();
		boardListResponseInfo.isBookMark = board.getBookMarkUser().stream()
				.map(bookmark -> bookmark.getUser().getId())
				.anyMatch(id -> id.equals(userId));
		boardListResponseInfo.boardTime = board.showBoardTimeComparedToNow();

		return boardListResponseInfo;
	}
}
