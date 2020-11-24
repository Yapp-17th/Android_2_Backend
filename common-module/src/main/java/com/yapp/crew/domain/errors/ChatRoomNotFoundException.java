package com.yapp.crew.domain.errors;

public class ChatRoomNotFoundException extends RuntimeException {

	public ChatRoomNotFoundException(Long chatRoomId) {
		super("Chatting Service - Cannot find chat room with id: " + chatRoomId);
	}
}
