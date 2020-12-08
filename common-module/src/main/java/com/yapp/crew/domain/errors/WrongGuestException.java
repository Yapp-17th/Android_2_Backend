package com.yapp.crew.domain.errors;

public class WrongGuestException extends RuntimeException {

	public WrongGuestException(long userId, long chatRoomId) {
		super(String.format("User(%d) is not the guest of chat room(%d)", userId, chatRoomId));
	}
}
