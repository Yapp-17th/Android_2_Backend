package com.yapp.crew.domain.errors;

public class GuestApplyNotFoundException extends RuntimeException {

	public GuestApplyNotFoundException(long userId, long boardId) {
		super(String.format("User(%d) did not apply to board(%d) yet", userId, boardId));
	}
}
