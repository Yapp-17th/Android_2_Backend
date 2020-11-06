package com.yapp.crew.domain.errors;

public class InactiveUserException extends RuntimeException {

	public InactiveUserException(String message) {
		super(message);
	}
}
