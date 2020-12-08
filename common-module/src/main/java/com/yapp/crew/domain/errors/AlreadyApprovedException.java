package com.yapp.crew.domain.errors;

public class AlreadyApprovedException extends RuntimeException {

	public AlreadyApprovedException(long userId, long boardId) {
		super(String.format("User(%d) is already approved to board(%d)", userId, boardId));
	}
}
