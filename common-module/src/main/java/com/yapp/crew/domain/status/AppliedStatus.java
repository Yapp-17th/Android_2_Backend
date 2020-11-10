package com.yapp.crew.domain.status;

public enum AppliedStatus {

	PENDING(0, "미신청"),
	APPLIED(1, "참여대기중"),
	APPROVED(2, "참여중");

	private final int code;
	private final String name;

	AppliedStatus(final int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
