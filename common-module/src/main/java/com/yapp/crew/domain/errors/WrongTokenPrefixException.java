package com.yapp.crew.domain.errors;

public class WrongTokenPrefixException extends RuntimeException {

	public WrongTokenPrefixException(String message) {
		super(message);
	}
}
