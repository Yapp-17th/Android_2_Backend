package com.yapp.crew.domain.errors;

public class BoardNotFoundException extends RuntimeException {

	public BoardNotFoundException(long boardId) {
		super("Cannot find board with id: " + boardId);
	}
}
