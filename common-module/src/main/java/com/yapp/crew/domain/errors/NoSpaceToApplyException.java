package com.yapp.crew.domain.errors;

public class NoSpaceToApplyException extends RuntimeException {

	public NoSpaceToApplyException(String message) {
		super(message);
	}
}
