package com.yapp.crew.domain.status;

import lombok.Getter;

@Getter
public enum BoardStatus {

	RECRUITING(0, "모집 중"),
	COMPLETE(1, "모집 완료"),
	FINISHED(2, "활동 종료"),
	CANCELED(3, "모임 취소");

	private final int code;
	private final String name;

	BoardStatus(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
