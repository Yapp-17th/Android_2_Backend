package com.yapp.crew.domain.type;

import com.yapp.crew.domain.errors.ReportCodeNotFoundException;

public enum ReportType {
	BOARD_NO_MANNERS(1, "욕설 및 악의성 내용이 포함돼있어요"),
	BOARD_BOTHERING_CONTENTS(2, "선정적인 내용이 포함돼있어요"),
	BOARD_ADVERTISE(3, "활동과 관련 없는 홍보성 글이에요"),
	USER_LOST_CONTACT(4, "중간에 연락이 두절됐어요"),
	USER_NO_PARTICIPATE(5, "활동에 참여하지 않았어요"),
	USER_LATE(6, "사전 연락없이 30분 이상 지각했어요"),
	USER_BOTHERING(7, "원치 않는 연락을 계속 보내요"),
	USER_NO_MANNERS(8, "욕설 및 비 매너 행위를 했어요"),
	USER_RUN(9, "대관비 등 활동 내 비용을 내지 않았어요"),
	USER_CANCELED(10, "활동을 중간에 취소했어요"),
	OTHERS(0, "기타");

	private final int code;
	private final String name;

	ReportType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public static ReportType valueOfCode(int code){
		for (ReportType reportType : ReportType.values()) {
			if (reportType.code == code) {
				return reportType;
			}
		}
		throw new ReportCodeNotFoundException((long) code);
	}
}
