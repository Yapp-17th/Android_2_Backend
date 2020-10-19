package com.yapp.crew.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EnumListSuccessDto {

  private String type;
  private int status;
  private String message;
  private List<String> data;

  public static EnumListSuccessDto builder(String type) {
    EnumListSuccessDto enumListSuccessDto = new EnumListSuccessDto();
    enumListSuccessDto.type = type;
    enumListSuccessDto.status = 200;
    enumListSuccessDto.message = "거주 지역 리스트 조회 성공";
    return enumListSuccessDto;
  }

  public void setData(List<String> data) {
    this.data = data;
  }
}
