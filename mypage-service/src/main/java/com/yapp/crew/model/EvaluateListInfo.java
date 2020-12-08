package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EvaluateListInfo {

	private long userId = -1L;
	private String nickName = "(알수없음)";
	private boolean isHost = false;
	private boolean isLike = false;
	private boolean isDislike = false;

	public static EvaluateListInfo build(Evaluation evaluation, User user, Board board) {
		EvaluateListInfo evaluateListInfo = new EvaluateListInfo();
		evaluateListInfo.isHost = board.getUser().getId() == user.getId();
		evaluateListInfo.isLike = evaluation.isLike();
		evaluateListInfo.isDislike = evaluation.isDislike();

		if (user.isValidUser()) {
			evaluateListInfo.userId = user.getId();
			evaluateListInfo.nickName = user.getNickname();
		}

		return evaluateListInfo;
	}
}
