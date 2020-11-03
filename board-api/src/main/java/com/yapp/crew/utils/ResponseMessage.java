package com.yapp.crew.utils;

public enum ResponseMessage {
  BOARD_POST_SUCCESS("글이 정상적으로 업로드 되었습니다"),
  BOARD_DELETE_SUCCESS("글이 정상적으로 삭제 되었습니다"),
  BOARD_DIFF_USERID("글쓴이와 사용자 아이디가 다릅니다."),
  BOARD_CONTENT_SUCCESS("글 내용 조회 성공"),

  BOOKMARK_POST_SUCCESS("북마크가 정상적으로 추가 되었습니다"),
  BOOKMARK_DELETE_SUCCESS("북마크가 정상적으로 삭제 되었습니다"),

  REPORT_SUCCESS("신고가 정상적으로 접수되었습니다."),
  HIDDEN_SUCCESS("게시물이 피드에서 숨김처리 되었습니다."),

  INACTIVE_USER_FAIL("회원 가입이 필요합니다."),
  SUSPENDED_USER_FAIL("신고로 정지된 사용자입니다."),
  INVALID_REQUEST_BODY("request body가 올바르지 않습니다"),
  INTERNAL_SERVER_FAIL("서버 내부 오류"),
  TOKEN_NOT_VALID("토큰이 유효하지 않습니다");

  private String message;

  ResponseMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
