package com.yapp.crew.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCancel {

	private Long boardId;

	private Long userId;

	public static BoardCancel build(Long boardId, Long userId) {
		BoardCancel payload = new BoardCancel();
		payload.boardId = boardId;
		payload.userId = userId;
		return payload;
	}
}
