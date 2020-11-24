package com.yapp.crew.domain.errors;

public class IsNotApprovedException extends RuntimeException {

	public IsNotApprovedException(Long userId, Long boardId) {
		super(String.format("User(%d) is not approved to board(%d)", userId, boardId));
	}
}
