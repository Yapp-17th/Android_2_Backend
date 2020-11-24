package com.yapp.crew.domain.errors;

public class BoardNotFoundException extends RuntimeException {

	public BoardNotFoundException(Long boardId) {
		super("Chatting Service - Cannot find board with id: " + boardId);
	}
}
