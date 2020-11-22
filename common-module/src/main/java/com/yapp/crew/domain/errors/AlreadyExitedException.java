package com.yapp.crew.domain.errors;

public class AlreadyExitedException extends RuntimeException {

	public AlreadyExitedException(String message) {
		super(message);
	}
}
