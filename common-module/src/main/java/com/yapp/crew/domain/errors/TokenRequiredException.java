package com.yapp.crew.domain.errors;

public class TokenRequiredException extends RuntimeException {

  public TokenRequiredException(String message) {
    super(message);
  }
}
