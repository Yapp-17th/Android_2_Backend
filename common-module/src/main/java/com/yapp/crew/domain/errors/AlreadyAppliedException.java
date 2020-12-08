package com.yapp.crew.domain.errors;

public class AlreadyAppliedException extends RuntimeException {

	public AlreadyAppliedException(long userId, long boardId) {
		super(String.format("User(%d) has already applied to board(%d)", userId, boardId));
	}
}
