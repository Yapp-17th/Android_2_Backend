package com.yapp.crew.domain.errors;

public class CannotApproveException extends RuntimeException {

	public CannotApproveException(long boardId) {
		super("Cannot approve to board with id: " + boardId);
	}
}
