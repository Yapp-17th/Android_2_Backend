package com.yapp.crew.utils;

public enum ResponseMessage {
  SUCCESS("리스트 조회 성공"),
  FAIL("리스트 조회 실패, 서버 내부 오류");

  private String message;

  ResponseMessage(String message){
    this.message=message;
  }

  public String getMessage() {
    return this.message;
  }
}
