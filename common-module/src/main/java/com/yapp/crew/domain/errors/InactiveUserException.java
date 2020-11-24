package com.yapp.crew.domain.errors;

public class InactiveUserException extends RuntimeException {

	public InactiveUserException(Long userId) {
		super("A inactive user tried to access with id: " + userId);
	}
}
