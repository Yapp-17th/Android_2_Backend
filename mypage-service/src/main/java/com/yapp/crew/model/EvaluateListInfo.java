package com.yapp.crew.model;

import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EvaluateListInfo {

	private Long userId;
	private String nickName;
	private Boolean isHost;
	private Boolean isLike;
	private Boolean isDislike;

	public static EvaluateListInfo build(Evaluation evaluation, User user, Board board) {
		EvaluateListInfo evaluateListInfo = new EvaluateListInfo();
		evaluateListInfo.userId = user.getId();
		evaluateListInfo.nickName = user.getNickname();
		evaluateListInfo.isHost = board.getUser().getId().equals(user.getId());
		evaluateListInfo.isLike = evaluation.getIsLike();
		evaluateListInfo.isDislike = evaluation.getIsDislike();

		return evaluateListInfo;
	}
}
