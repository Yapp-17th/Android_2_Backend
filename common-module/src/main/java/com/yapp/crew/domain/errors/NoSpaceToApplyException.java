package com.yapp.crew.domain.errors;

public class NoSpaceToApplyException extends RuntimeException {

	public NoSpaceToApplyException(long boardId) {
		super("This board has no space to apply with id: " + boardId);
	}
}
