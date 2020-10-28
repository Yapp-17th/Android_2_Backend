package com.yapp.crew.dto;

import com.yapp.crew.model.LoginResponse;
import com.yapp.crew.model.LoginResponseBody;
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

  public static LoginResponseDto build(LoginResponseBody loginResponseBody) {
    LoginResponseDto loginResponseDto = new LoginResponseDto();
    loginResponseDto.status = loginResponseBody.getStatus();
    loginResponseDto.isSuccess = loginResponseBody.isSuccess();
    loginResponseDto.message = loginResponseBody.getMessage();
    return loginResponseDto;
  }
}
