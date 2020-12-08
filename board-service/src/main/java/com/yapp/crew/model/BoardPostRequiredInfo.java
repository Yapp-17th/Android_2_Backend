package com.yapp.crew.model;

import com.yapp.crew.dto.request.BoardInfoRequestDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostRequiredInfo {

	private String title;
	private String content;
	private long category;
	private long city;
	private long userTag;
	private int recruitNumber;
	private LocalDateTime date;
	private String place;

	public static BoardPostRequiredInfo build(BoardInfoRequestDto boardInfoRequestDto) {
		BoardPostRequiredInfo boardPostRequiredInfo = new BoardPostRequiredInfo();
		boardPostRequiredInfo.title = boardInfoRequestDto.getTitle();
		boardPostRequiredInfo.content = boardInfoRequestDto.getContent();
		boardPostRequiredInfo.category = boardInfoRequestDto.getCategory();
		boardPostRequiredInfo.city = boardInfoRequestDto.getCity();
		boardPostRequiredInfo.userTag = boardInfoRequestDto.getUserTag();
		boardPostRequiredInfo.recruitNumber = boardInfoRequestDto.getRecruitNumber();
		boardPostRequiredInfo.date = boardInfoRequestDto.getDate();
		boardPostRequiredInfo.place = boardInfoRequestDto.getPlace();

		return boardPostRequiredInfo;
	}
}
