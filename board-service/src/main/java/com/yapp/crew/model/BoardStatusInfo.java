package com.yapp.crew.model;

import com.yapp.crew.domain.status.BoardStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardStatusInfo {

	Integer code;
	String name;

	public static BoardStatusInfo build(BoardStatus boardStatus) {
		BoardStatusInfo boardStatusInfo = new BoardStatusInfo();
		boardStatusInfo.code = boardStatus.getCode();
		boardStatusInfo.name = boardStatus.getName();

		return boardStatusInfo;
	}
}
