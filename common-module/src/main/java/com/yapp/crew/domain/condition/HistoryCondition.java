package com.yapp.crew.domain.condition;

import com.yapp.crew.domain.type.HistoryType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HistoryCondition {

	private long userId;
	HistoryType historyType;

	public static HistoryCondition build(long userId, String name) {
		HistoryCondition historyCondition = new HistoryCondition();
		historyCondition.userId = userId;
		historyCondition.historyType = HistoryType.getHistoryType(name);

		return historyCondition;
	}

	public static HistoryCondition build(long userId, HistoryType type) {
		HistoryCondition historyCondition = new HistoryCondition();
		historyCondition.userId = userId;
		historyCondition.historyType = type;

		return historyCondition;
	}
}
