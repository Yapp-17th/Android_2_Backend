package com.yapp.crew.model;

import com.yapp.crew.dto.BoardRequestDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostRequiredInfo {

  private String title;
  private String content;
  private Long category;
  private Long city;
  private Long userTag;
  private Integer recruitNumber;
  private LocalDateTime date;
  private String place;

  public static BoardPostRequiredInfo build(BoardRequestDto boardRequestDto) {
    BoardPostRequiredInfo boardPostRequiredInfo = new BoardPostRequiredInfo();
    boardPostRequiredInfo.title = boardRequestDto.getTitle();
    boardPostRequiredInfo.content = boardRequestDto.getContent();
    boardPostRequiredInfo.category = boardRequestDto.getCategory();
    boardPostRequiredInfo.city = boardRequestDto.getCity();
    boardPostRequiredInfo.userTag = boardRequestDto.getUserTag();
    boardPostRequiredInfo.recruitNumber = boardRequestDto.getRecruitNumber();
    boardPostRequiredInfo.date = boardRequestDto.getDate();
    boardPostRequiredInfo.place = boardRequestDto.getPlace();

    return boardPostRequiredInfo;
  }
}
