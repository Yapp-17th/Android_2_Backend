package com.yapp.crew.domain.errors;

public class GuestApplyNotFoundException extends RuntimeException {

	public GuestApplyNotFoundException(Long userId, Long boardId) {
		super(String.format("User(%d) did not apply to board(%d) yet", userId, boardId));
	}
}
