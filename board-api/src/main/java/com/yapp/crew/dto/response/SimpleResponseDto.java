package com.yapp.crew.dto.response;

import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.SimpleResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SimpleResponseDto {

  private int status;
  private boolean isSuccess;
  private String message;

  public static SimpleResponseDto build(SimpleResponse simpleResponse) {
    SimpleResponseDto simpleResponseDto = new SimpleResponseDto();
    simpleResponseDto.status = simpleResponse.getStatus();
    simpleResponseDto.isSuccess = simpleResponse.isSuccess();
    simpleResponseDto.message = simpleResponse.getMessage();
    return simpleResponseDto;
  }
}
