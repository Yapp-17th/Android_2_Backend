package com.yapp.crew.domain.errors;

public class CannotDisapproveException extends RuntimeException {

	public CannotDisapproveException(String message) {
		super(message);
	}
}
