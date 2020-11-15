package com.yapp.crew.domain.errors;

public class UnAuthorizedEventException extends RuntimeException {

	public UnAuthorizedEventException(String message) {
		super(message);
	}
}
