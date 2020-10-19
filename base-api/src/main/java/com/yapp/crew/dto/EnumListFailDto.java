package com.yapp.crew.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EnumListFailDto {

  private String type;
  private int status;
  private String message;

  public static EnumListFailDto builder(String type) {
    EnumListFailDto enumListFailDto = new EnumListFailDto();
    enumListFailDto.type = type;
    enumListFailDto.status = 500;
    enumListFailDto.message = "거주 지역 리스트 조회 실패";
    return enumListFailDto;
  }

  public void addMessage(String message) {
    this.message = String.join(",", this.message, message);
  }
}
