package com.yapp.crew.model;

import com.yapp.crew.network.model.SimpleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

@AllArgsConstructor
@Getter
public class UserAuthResponse {

  HttpHeaders httpHeaders;
  SimpleResponse simpleResponse;

  public UserAuthResponse(SimpleResponse simpleResponse) {
    this.simpleResponse = simpleResponse;
  }
}
