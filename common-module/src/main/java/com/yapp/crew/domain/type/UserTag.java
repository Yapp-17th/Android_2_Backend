package com.yapp.crew.domain.type;

public enum UserTag {
  TIGHT_USER("빡겜러"),
  FUN_USER("즐겜러");

  private final String name;

  UserTag(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
