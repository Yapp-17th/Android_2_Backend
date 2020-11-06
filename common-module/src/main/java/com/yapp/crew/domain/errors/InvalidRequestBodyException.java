package com.yapp.crew.domain.errors;

public class InvalidRequestBodyException extends RuntimeException {

	public InvalidRequestBodyException(String message) {
		super(message);
	}
}
