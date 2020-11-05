package com.yapp.crew.domain.errors;

public class AddressNotFoundException extends RuntimeException {

  public AddressNotFoundException(String message) {
    super(message);
  }
}
