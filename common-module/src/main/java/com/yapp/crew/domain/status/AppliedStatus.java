package com.yapp.crew.domain.status;

public enum AppliedStatus {

	PENDING(0),
	APPROVED(1);

	private final int code;

	AppliedStatus(final int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
