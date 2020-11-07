package com.yapp.crew.domain.status;

public enum AppliedStatus {

	PENDING(0),
	APPLIED(1),
	APPROVED(2);

	private final int code;

	AppliedStatus(final int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
