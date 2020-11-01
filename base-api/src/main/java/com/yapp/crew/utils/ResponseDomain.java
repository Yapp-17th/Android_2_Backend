package com.yapp.crew.utils;

public enum ResponseDomain {
  ADDRESSCITY("city"),
  EXERCISE("exercise"),
  USER_TAG("userTag"),
  BOARD_REPORT_TYPE("boardReportType"),
  USER_REPORT_TYPE("userReportType");

  private String name;

  ResponseDomain(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
