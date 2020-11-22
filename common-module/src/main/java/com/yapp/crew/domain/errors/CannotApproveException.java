package com.yapp.crew.domain.errors;

public class CannotApproveException extends RuntimeException {

	public CannotApproveException(String message) {
		super(message);
	}
}
