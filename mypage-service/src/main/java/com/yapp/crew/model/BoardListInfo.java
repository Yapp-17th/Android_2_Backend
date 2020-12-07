package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.status.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardListInfo {

	private Long boardId = -1L;
	private Long hostId = -1L;
	private String hostName = "(알수없음)";
	private String title = "";
	private String groupStatus = "";
	private String exercise = "";
	private String city = "";
	private Boolean isBookMark = true;
	private int recruitNumber = 0;
	private int recruitedNumber = 0;
	private String time = "";

	public static BoardListInfo build(Board board) {
		BoardListInfo boardListInfo = new BoardListInfo();
		boardListInfo.boardId = board.getId();
		boardListInfo.title = board.getTitle();
		boardListInfo.groupStatus = board.getStatus().getName();
		boardListInfo.exercise = board.getCategory().getExercise().getName();
		boardListInfo.city = board.getAddress().getCity().getName();
		boardListInfo.recruitNumber = board.getRecruitCount();
		boardListInfo.recruitedNumber = board.getApprovedCount();
		boardListInfo.time = board.showBoardTimeComparedToNow();

		if (board.getUser().isValidUser()) {
			boardListInfo.hostId = board.getUser().getId();
			boardListInfo.hostName = board.getUser().getNickname();
		}

		return boardListInfo;
	}
}
