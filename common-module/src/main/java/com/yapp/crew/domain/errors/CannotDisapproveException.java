package com.yapp.crew.domain.errors;

public class CannotDisapproveException extends RuntimeException {

	public CannotDisapproveException(long boardId) {
		super("Cannot approve to board with id: " + boardId);
	}
}
