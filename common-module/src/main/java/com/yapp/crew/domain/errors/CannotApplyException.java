package com.yapp.crew.domain.errors;

public class CannotApplyException extends RuntimeException {

	public CannotApplyException(Long boardId) {
		super("Chatting Service - Cannot apply to board with id: " + boardId);
	}
}
