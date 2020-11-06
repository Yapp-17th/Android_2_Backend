package com.yapp.crew.domain.errors;

public class UserDuplicateFieldException extends RuntimeException {

	public UserDuplicateFieldException(String message) {
		super(message);
	}
}
