package com.yapp.crew.domain.errors;

public class TagNotFoundException extends RuntimeException {

  public TagNotFoundException(String message) {
    super(message);
  }
}
