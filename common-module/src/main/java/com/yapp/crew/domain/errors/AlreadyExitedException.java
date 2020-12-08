package com.yapp.crew.domain.errors;

public class AlreadyExitedException extends RuntimeException {

	public AlreadyExitedException(long userId, long chatRoomId) {
		super(String.format("User(%d) already exited chat room(%d)", userId, chatRoomId));
	}
}
