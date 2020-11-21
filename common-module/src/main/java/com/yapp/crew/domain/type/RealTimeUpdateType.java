package com.yapp.crew.domain.type;

import lombok.Getter;

@Getter
public enum RealTimeUpdateType {

	APPLIED(0, "게시글 신청"),
	APPROVED(1, "사용자 승인"),
	DISAPPROVED(2, "사용자 거절"),
	MESSAGE_READ(3, "메시지 읽음"),
	USER_EXITED(4, "채팅방 나가기");

	private final int code;
	private final String name;

	RealTimeUpdateType(int code, String name) {
		this.code = code;
		this.name = name;
	}
}
