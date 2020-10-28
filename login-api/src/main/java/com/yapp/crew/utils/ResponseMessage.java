package com.yapp.crew.utils;

public enum ResponseMessage {
  SIGNIN_SUCCESS("로그인 성공"),
  SIGNIN_FAIL_NEEDS_SIGN_UP("로그인 실패, 회원 가입 필요"),
  SIGNIN_FAIL("로그인 실패, 서버 내부 오류"),

  SIGNUP_SUCCESS("회원 가입 성공"),
  SIGNUP_FAIL("회원 가입 실패, 서버 내부 오류"),

  SIGNOUT_SUCCESS("로그아웃 성공"),
  SIGNOUT_FAIL("로그아웃 실패"),

  TOKEN_NOT_CREATED("토큰이 생성되지 않았습니다");

  private String message;

  ResponseMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}

