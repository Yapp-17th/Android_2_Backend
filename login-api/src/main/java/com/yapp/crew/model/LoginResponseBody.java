package com.yapp.crew.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class LoginResponseBody {

  private int status;
  private boolean isSuccess;
  private String message;

  public static LoginResponseBody pass(String message) {
    LoginResponseBody loginResponseBody = new LoginResponseBody();
    loginResponseBody.status = HttpStatus.OK.value();
    loginResponseBody.isSuccess = true;
    loginResponseBody.message = message;
    return loginResponseBody;
  }

  public static LoginResponseBody fail(String message) {
    LoginResponseBody loginResponseBody = new LoginResponseBody();
    loginResponseBody.status = HttpStatus.BAD_REQUEST.value();
    loginResponseBody.isSuccess = false;
    loginResponseBody.message = message;
    return loginResponseBody;
  }
}
