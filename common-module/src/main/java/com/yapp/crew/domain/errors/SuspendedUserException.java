package com.yapp.crew.domain.errors;

public class SuspendedUserException extends RuntimeException {

  public SuspendedUserException(String message) {
    super(message);
  }
}
