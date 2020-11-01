package com.yapp.crew.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

@AllArgsConstructor
@Getter
public class UserAuthResponse {

  HttpHeaders httpHeaders;
  UserAuthResponseBody userAuthResponseBody;

  public UserAuthResponse(UserAuthResponseBody userAuthResponseBody) {
    this.userAuthResponseBody = userAuthResponseBody;
  }
}
