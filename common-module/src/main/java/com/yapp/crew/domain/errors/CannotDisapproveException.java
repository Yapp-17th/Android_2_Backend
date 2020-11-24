package com.yapp.crew.domain.errors;

public class CannotDisapproveException extends RuntimeException {

	public CannotDisapproveException(Long boardId) {
		super("Chatting Service - Cannot approve to board with id: " + boardId);
	}
}
