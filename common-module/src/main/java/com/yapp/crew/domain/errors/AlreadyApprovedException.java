package com.yapp.crew.domain.errors;

public class AlreadyApprovedException extends RuntimeException {

	public AlreadyApprovedException(String message) {
		super(message);
	}
}
