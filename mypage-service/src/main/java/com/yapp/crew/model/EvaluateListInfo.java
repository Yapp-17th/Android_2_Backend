package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EvaluateListInfo {

	private Long userId = -1L;
	private String nickName = "(알수없음)";
	private Boolean isHost = false;
	private Boolean isLike = false;
	private Boolean isDislike = false;

	public static EvaluateListInfo build(Evaluation evaluation, User user, Board board) {
		EvaluateListInfo evaluateListInfo = new EvaluateListInfo();
		evaluateListInfo.isHost = board.getUser().getId().equals(user.getId());
		evaluateListInfo.isLike = evaluation.getIsLike();
		evaluateListInfo.isDislike = evaluation.getIsDislike();

		if (user.isValidUser()) {
			evaluateListInfo.userId = user.getId();
			evaluateListInfo.nickName = user.getNickname();
		}

		return evaluateListInfo;
	}
}
