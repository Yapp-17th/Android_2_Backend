package com.yapp.crew.domain.errors;

public class AlreadyAppliedException extends RuntimeException {

	public AlreadyAppliedException(String message) {
		super(message);
	}
}
