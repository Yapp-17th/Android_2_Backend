package com.yapp.crew.domain.errors;

public class WrongHostException extends RuntimeException {

	public WrongHostException(String message) {
		super(message);
	}
}
