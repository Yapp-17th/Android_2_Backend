package com.yapp.crew.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

@AllArgsConstructor
@Getter
public class LoginResponse {

  HttpHeaders httpHeaders;
  LoginResponseBody loginResponseBody;

  public LoginResponse(LoginResponseBody loginResponseBody) {
    this.loginResponseBody = loginResponseBody;
  }
}
