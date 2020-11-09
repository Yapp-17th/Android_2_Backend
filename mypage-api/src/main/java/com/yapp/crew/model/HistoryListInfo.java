package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
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

	public static HistoryListInfo build(Board board, User user){
		HistoryListInfo historyListInfo = new HistoryListInfo();
		historyListInfo.userName = user.getNickname();
		historyListInfo.leftTime = ""; // TODO: rebase 한 뒤에 추가
		historyListInfo.boardInfo = BoardListInfo.build(board);

		return historyListInfo;
	}
}
