package com.yapp.crew.domain.errors;

public class MessageNotFoundException extends RuntimeException {

	public MessageNotFoundException(String message) {
		super(message);
	}
}
