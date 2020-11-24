package com.yapp.crew.domain.errors;

public class WrongGuestException extends RuntimeException {

	public WrongGuestException(Long userId, Long chatRoomId) {
		super(String.format("Chatting Service - User(%d) is not the guest of chat room(%d)", userId, chatRoomId));
	}
}
