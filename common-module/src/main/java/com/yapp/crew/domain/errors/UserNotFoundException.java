package com.yapp.crew.domain.errors;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(Long userId) {
		super("Cannot find user with id: " + userId);
	}
}
