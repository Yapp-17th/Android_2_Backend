package com.yapp.crew.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class SimpleResponse {

  private int status;
  private boolean isSuccess;
  private String message;

  public static SimpleResponse pass(String message) {
    SimpleResponse simpleResponse = new SimpleResponse();
    simpleResponse.status = HttpStatus.OK.value();
    simpleResponse.isSuccess = true;
    simpleResponse.message = message;
    return simpleResponse;
  }

  public static SimpleResponse fail(HttpStatus httpStatus, String message) {
    SimpleResponse simpleResponse = new SimpleResponse();
    simpleResponse.status = httpStatus.value();
    simpleResponse.isSuccess = false;
    simpleResponse.message = message;
    return simpleResponse;
  }
}
