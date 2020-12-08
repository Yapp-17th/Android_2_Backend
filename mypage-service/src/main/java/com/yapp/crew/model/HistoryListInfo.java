package com.yapp.crew.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.BoardStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HistoryListInfo {

	@JsonProperty(value = "isHost")
	private boolean isHost = false;

	private String nickName = "(알수없음)";

	@JsonProperty(value = "isContinue")
	private boolean isContinue = false;

	private BoardListInfo boardInfo;

	public static HistoryListInfo build(Board board, User user) {
		HistoryListInfo historyListInfo = new HistoryListInfo();
		historyListInfo.isHost = board.getUser().getId().equals(user.getId());
		historyListInfo.isContinue = board.getStatus() != BoardStatus.RECRUITING;
		historyListInfo.boardInfo = BoardListInfo.build(board);

		if (user.isValidUser()) {
			historyListInfo.nickName = user.getNickname();
		}
		return historyListInfo;
	}
}
