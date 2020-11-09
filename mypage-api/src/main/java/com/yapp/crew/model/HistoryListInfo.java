package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.GroupStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HistoryListInfo {

	private boolean isHost;
	private String userName;
	private boolean isContinue;
	private String leftTime;
	private BoardListInfo boardInfo;

	public static HistoryListInfo build(Board board, User user) {
		HistoryListInfo historyListInfo = new HistoryListInfo();
		historyListInfo.isHost = board.getUser().getId().equals(user.getId());
		historyListInfo.userName = user.getNickname();
		historyListInfo.isContinue = board.getGroupStatus() != GroupStatus.RECRUITING;
		historyListInfo.leftTime = ""; // TODO: rebase 한 뒤에 추가
		historyListInfo.boardInfo = BoardListInfo.build(board);

		return historyListInfo;
	}
}
