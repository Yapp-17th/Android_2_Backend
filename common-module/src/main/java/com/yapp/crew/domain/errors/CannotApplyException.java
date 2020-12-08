package com.yapp.crew.domain.errors;

public class CannotApplyException extends RuntimeException {

	public CannotApplyException(long boardId) {
		super("Cannot apply to board with id: " + boardId);
	}
}
