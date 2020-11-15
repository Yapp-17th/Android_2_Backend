package com.yapp.crew.domain.errors;

public class InternalServerErrorException extends RuntimeException {

	public InternalServerErrorException(String message) {
		super(message);
	}
}
