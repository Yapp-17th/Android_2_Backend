package com.yapp.crew.dto;

import com.yapp.crew.model.UserAuthResponseBody;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserAuthResponseDto {

  private int status;
  private boolean isSuccess;
  private String message;

  public static UserAuthResponseDto build(UserAuthResponseBody userAuthResponseBody) {
    UserAuthResponseDto userAuthResponseDto = new UserAuthResponseDto();
    userAuthResponseDto.status = userAuthResponseBody.getStatus();
    userAuthResponseDto.isSuccess = userAuthResponseBody.isSuccess();
    userAuthResponseDto.message = userAuthResponseBody.getMessage();
    return userAuthResponseDto;
  }
}
