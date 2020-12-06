package com.yapp.crew.domain.errors;

public class CannotApplyToMyBoardException extends RuntimeException {

	public CannotApplyToMyBoardException(Long userId, Long boardId) {
		super(String.format("User(%d) cannot apply to my board(%d)", userId, boardId));
	}
}
