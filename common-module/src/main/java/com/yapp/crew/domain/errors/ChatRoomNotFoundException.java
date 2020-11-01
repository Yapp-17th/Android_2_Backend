package com.yapp.crew.domain.errors;

public class ChatRoomNotFoundException extends RuntimeException {

	public ChatRoomNotFoundException(String message) {
		super(message);
	}
}
