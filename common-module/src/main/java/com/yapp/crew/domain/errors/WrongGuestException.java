package com.yapp.crew.domain.errors;

public class WrongGuestException extends RuntimeException {

	public WrongGuestException(String message) {
		super(message);
	}
}
