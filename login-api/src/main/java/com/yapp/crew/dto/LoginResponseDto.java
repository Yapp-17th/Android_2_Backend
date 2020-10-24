package com.yapp.crew.dto;

import com.yapp.crew.utils.ResponseDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class LoginResponseDto {

  private int status;
  private boolean isSuccess;
  private String message;

  public static LoginResponseDto pass(String message) {
    LoginResponseDto loginResponseDto = new LoginResponseDto();
    loginResponseDto.status = HttpStatus.OK.value();
    loginResponseDto.isSuccess = true;
    loginResponseDto.message = message;
    return loginResponseDto;
  }

  public static LoginResponseDto fail(String message) {
    LoginResponseDto loginResponseDto = new LoginResponseDto();
    loginResponseDto.status = HttpStatus.BAD_REQUEST.value();
    loginResponseDto.isSuccess = false;
    loginResponseDto.message = message;
    return loginResponseDto;
  }
}
