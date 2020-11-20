package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.BoardStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HistoryListInfo {

	private Boolean isHost;
	private String userName;
	private Boolean isContinue;
	private String leftTime;
	private BoardListInfo boardInfo;

	public static HistoryListInfo build(Board board, User user) {
		HistoryListInfo historyListInfo = new HistoryListInfo();
		historyListInfo.isHost = board.getUser().getId().equals(user.getId());
		historyListInfo.userName = user.getNickname();
		historyListInfo.isContinue = board.getStatus() != BoardStatus.RECRUITING;
		historyListInfo.leftTime = board.showBoardTimeComparedToNow();
		historyListInfo.boardInfo = BoardListInfo.build(board);

		return historyListInfo;
	}
}
