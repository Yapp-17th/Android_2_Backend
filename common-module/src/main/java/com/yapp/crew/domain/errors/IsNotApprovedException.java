package com.yapp.crew.domain.errors;

public class IsNotApprovedException extends RuntimeException {

	public IsNotApprovedException(long userId, long boardId) {
		super(String.format("User(%d) is not approved to board(%d)", userId, boardId));
	}
}
