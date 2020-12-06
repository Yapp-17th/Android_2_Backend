package com.yapp.crew.domain.errors;

public class ForbiddenUserSignUpException extends RuntimeException {

	public ForbiddenUserSignUpException(Long userId) {
		super("A forbidden user tried to sign up with id: " + userId);
	}
}
