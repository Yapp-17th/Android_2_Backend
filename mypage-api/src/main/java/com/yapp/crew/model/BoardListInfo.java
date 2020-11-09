package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardListInfo {

	private Long boardId;
	private Long hostId;
	private String hostName;
	private String title;
	private String groupStatus;
	private String exercise;
	private String city;
	private boolean isBookMar = true;
	private int recruitNumber;
	private int recruitedNumber;
	private String time;

	public static BoardListInfo build(Board board, User user) {
		BoardListInfo boardListInfo = new BoardListInfo();
		boardListInfo.boardId = board.getId();
		boardListInfo.hostId = board.getUser().getId();
		boardListInfo.hostName = board.getUser().getNickname();
		boardListInfo.title = board.getTitle();
		boardListInfo.groupStatus = board.getGroupStatus().getName(); // TODO: 나중에 바꾸기
		boardListInfo.exercise = board.getCategory().getExercise().getName();
		boardListInfo.city = board.getAddress().getCity().getName();
		boardListInfo.recruitNumber = board.getRecruitCount();
		boardListInfo.recruitedNumber = board.getApprovedCount();
		boardListInfo.time = ""; // TODO: rebase 후 업데이트

		return boardListInfo;
	}
}
