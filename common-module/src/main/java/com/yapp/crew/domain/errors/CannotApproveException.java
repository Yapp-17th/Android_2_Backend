package com.yapp.crew.domain.errors;

public class CannotApproveException extends RuntimeException {

	public CannotApproveException(Long boardId) {
		super("Chatting Service - Cannot approve to board with id: " + boardId);
	}
}
