package com.yapp.crew.domain.errors;

public class AlreadyAppliedException extends RuntimeException {

	public AlreadyAppliedException(Long userId, Long boardId) {
		super(String.format("User(%d) has already applied to board(%d)", userId, boardId));
	}
}
