package com.yapp.crew.domain.errors;

public class MessageNotFoundException extends RuntimeException {

	public MessageNotFoundException(Long messageId) {
		super("Chatting Service - Cannot find message with id: " + messageId);
	}
}
