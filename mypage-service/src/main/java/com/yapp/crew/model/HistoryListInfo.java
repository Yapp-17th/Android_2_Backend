package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.status.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HistoryListInfo {

	private Boolean isHost = false;
	private String nickName = "(알수없음)";
	private Boolean isContinue = false;
	private BoardListInfo boardInfo;

	public static HistoryListInfo build(Board board, User user) {
		HistoryListInfo historyListInfo = new HistoryListInfo();
		historyListInfo.isHost = board.getUser().getId().equals(user.getId());
		historyListInfo.isContinue = board.getStatus() != BoardStatus.RECRUITING;
		historyListInfo.boardInfo = BoardListInfo.build(board);

		if (user.getStatus() == UserStatus.ACTIVE || user.getStatus() == UserStatus.SUSPENDED) {
			historyListInfo.nickName = user.getNickname();
		}

		return historyListInfo;
	}
}
