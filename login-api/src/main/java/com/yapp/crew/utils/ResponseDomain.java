package com.yapp.crew.utils;

public enum ResponseDomain {
  SIGN_IN("sign in"),
  SIGN_OUT("sign out"),
  SIGN_UP("sign up");

  private String name;

  ResponseDomain(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
