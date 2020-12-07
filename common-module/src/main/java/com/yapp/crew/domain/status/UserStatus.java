package com.yapp.crew.domain.status;

public enum UserStatus {
	ACTIVE(1),
	INACTIVE(0),
	SUSPENDED(-1),
	FORBIDDEN(-2);

	private final int code;

	UserStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
