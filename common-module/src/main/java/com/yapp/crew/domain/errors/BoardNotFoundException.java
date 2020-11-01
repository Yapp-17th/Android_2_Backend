package com.yapp.crew.domain.errors;

public class BoardNotFoundException extends RuntimeException {

	public BoardNotFoundException(String message) {
		super(message);
	}
}
