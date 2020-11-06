package com.yapp.crew.model;

import com.yapp.crew.domain.status.GroupStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardStatusInfo {

	int code;
	String name;

	public static BoardStatusInfo build(GroupStatus groupStatus) {
		BoardStatusInfo boardStatusInfo = new BoardStatusInfo();
		boardStatusInfo.code = groupStatus.getCode();
		boardStatusInfo.name = groupStatus.getName();

		return boardStatusInfo;
	}
}
