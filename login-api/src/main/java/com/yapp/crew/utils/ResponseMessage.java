package com.yapp.crew.utils;

public enum ResponseMessage {
  SIGNIN_SUCCESS("로그인 성공"),
  DUPLICATE_EMAIL("중복된 이메일 입니다."),
  DUPLICATE_NICKNAME("중복된 닉네임 입니다."),

  SIGNUP_SUCCESS("회원가입을 축하드립니다!\n운동플래닛과 건강한 운동생활을\n시작해보세요!"),
  SIGNUP_FAIL("회원 가입 실패, 서버 내부 오류"),

  INACTIVE_USER_FAIL("회원 가입이 필요합니다."),
  SUSPENDED_USER_FAIL("신고로 정지된 사용자입니다."),
  INVALID_REQUEST_BODY("request body가 올바르지 않습니다"),
  INTERNAL_SERVER_FAIL("서버 내부 오류"),

  TOKEN_NOT_CREATED("토큰이 생성되지 않았습니다"),
  TOKEN_NOT_VALID("토큰이 유효하지 않습니다"),

  USER_CANNOT_FOUND("해당 사용자를 찾을 수 없습니다."),
  WITHDRAW_SUCCESS("회원 탈퇴 성공");

  private String message;

  ResponseMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}

