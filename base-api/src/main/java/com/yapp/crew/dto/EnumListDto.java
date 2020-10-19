package com.yapp.crew.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EnumListDto {

  private EnumListSuccessDto result;
  private EnumListFailDto error;

  public static EnumListDto pass(EnumListSuccessDto enumListSuccessDto) {
    EnumListDto enumListDto = new EnumListDto();
    enumListDto.result = enumListSuccessDto;
    enumListDto.error = null;
    return enumListDto;
  }

  public static EnumListDto fail(EnumListFailDto enumListFailDto) {
    EnumListDto enumListDto = new EnumListDto();
    enumListDto.result = null;
    enumListDto.error = enumListFailDto;
    return enumListDto;
  }
}
