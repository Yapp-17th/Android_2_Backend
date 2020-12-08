package com.yapp.crew.domain.errors;

public class MessageNotFoundException extends RuntimeException {

	public MessageNotFoundException(long messageId) {
		super("Cannot find message with id: " + messageId);
	}
}
