package com.yapp.crew.domain.errors;

public class AlreadyApprovedException extends RuntimeException {

	public AlreadyApprovedException(Long userId, Long boardId) {
		super(String.format("User(%d) is already approved to board(%d)", userId, boardId));
	}
}
