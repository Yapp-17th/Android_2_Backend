package com.yapp.crew.domain.errors;

public class CannotApplyToMyBoardException extends RuntimeException {

	public CannotApplyToMyBoardException(long userId, long boardId) {
		super(String.format("User(%d) cannot apply to my board(%d)", userId, boardId));
	}
}
