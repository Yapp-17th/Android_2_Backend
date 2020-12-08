package com.yapp.crew.domain.errors;

public class SuspendedUserException extends RuntimeException {

	public SuspendedUserException(long userId) {
		super("A suspended user tried to access with id: " + userId);
	}
}
