package com.yapp.crew.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class UserAuthResponseBody {

  private int status;
  private boolean isSuccess;
  private String message;

  public static UserAuthResponseBody pass(String message) {
    UserAuthResponseBody userAuthResponseBody = new UserAuthResponseBody();
    userAuthResponseBody.status = HttpStatus.OK.value();
    userAuthResponseBody.isSuccess = true;
    userAuthResponseBody.message = message;
    return userAuthResponseBody;
  }

  public static UserAuthResponseBody fail(HttpStatus httpStatus, String message) {
    UserAuthResponseBody userAuthResponseBody = new UserAuthResponseBody();
    userAuthResponseBody.status = httpStatus.value();
    userAuthResponseBody.isSuccess = false;
    userAuthResponseBody.message = message;
    return userAuthResponseBody;
  }
}
