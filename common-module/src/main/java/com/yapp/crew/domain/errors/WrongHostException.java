package com.yapp.crew.domain.errors;

public class WrongHostException extends RuntimeException {

	public WrongHostException(Long userId, Long typeId, String type) {
		super(String.format("User(%d) is not the host of %s(%d)", userId, type, typeId));
	}
}
