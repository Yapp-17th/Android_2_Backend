package com.yapp.crew.config;

public enum ResponseDomain {
  ADDRESSCITY("city"),
  EXERCISE("exercise");

  private String name;
  ResponseDomain(String name){
    this.name=name;
  }

  public String getName() {
    return this.name;
  }
}
