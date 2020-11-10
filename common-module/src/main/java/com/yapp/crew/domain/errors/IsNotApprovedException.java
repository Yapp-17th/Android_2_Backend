package com.yapp.crew.domain.errors;

public class IsNotApprovedException extends RuntimeException {

	public IsNotApprovedException(String message) {
		super(message);
	}
}
