package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardListInfo {

	private Long boardId;
	private String title;
	private String groupStatus;
	private String exercise;
	private String city;
	private int recruitNumber;
	private int recruitedNumber;

	public static BoardListInfo build(Board board) {
		BoardListInfo boardListInfo = new BoardListInfo();
		boardListInfo.boardId = board.getId();
		boardListInfo.title = board.getTitle();
		boardListInfo.groupStatus = board.getGroupStatus().getName(); // TODO: 나중에 바꾸기
		boardListInfo.exercise = board.getCategory().getExercise().getName();
		boardListInfo.city = board.getAddress().getCity().getName();
		boardListInfo.recruitNumber = board.getRecruitCount();
		boardListInfo.recruitedNumber = board.getApprovedCount();

		return boardListInfo;
	}
}
