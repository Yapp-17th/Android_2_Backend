package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EvaluateListInfo {

	private long userId;
	private String userName;
	private boolean isHost;
	private boolean isLike;
	private boolean isDislike;

	public static EvaluateListInfo build(Evaluation evaluation, User user, Board board) {
		EvaluateListInfo evaluateListInfo = new EvaluateListInfo();
		evaluateListInfo.userId = user.getId();
		evaluateListInfo.userName = user.getNickname();
		evaluateListInfo.isHost = board.getUser().getId().equals(user.getId());
		evaluateListInfo.isLike = evaluation.getIsLike();
		evaluateListInfo.isDislike = evaluation.getIsDislike();

		return evaluateListInfo;
	}
}
