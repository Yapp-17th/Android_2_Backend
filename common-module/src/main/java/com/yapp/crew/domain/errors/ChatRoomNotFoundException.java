package com.yapp.crew.domain.errors;

public class ChatRoomNotFoundException extends RuntimeException {

	public ChatRoomNotFoundException(Long chatRoomId) {
		super("Cannot find chat room with id: " + chatRoomId);
	}
}
