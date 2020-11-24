package com.yapp.crew.domain.errors;

public class AlreadyExitedException extends RuntimeException {

	public AlreadyExitedException(Long userId, Long chatRoomId) {
		super(String.format("Chatting Service - User(%d) already exited chat room(%d)", userId, chatRoomId));
	}
}
