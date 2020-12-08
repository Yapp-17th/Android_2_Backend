package com.yapp.crew.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCancel {

	private long boardId;

	private long userId;

	public static BoardCancel build(long boardId, long userId) {
		BoardCancel payload = new BoardCancel();
		payload.boardId = boardId;
		payload.userId = userId;
		return payload;
	}
}
