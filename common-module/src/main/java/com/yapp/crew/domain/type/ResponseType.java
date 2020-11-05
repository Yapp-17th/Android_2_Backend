package com.yapp.crew.domain.type;

import lombok.Getter;

@Getter
public enum ResponseType {

  SUCCESS("성공적으로 API를 호출했습니다"),
  TOKEN_REQUIRED("JWT 토큰과 함께 API를 호출하시기 바랍니다"),
  WRONG_TOKEN_PREFIX("Bearer 타입 토큰을 전달하시기 바랍니다"),
  EXPIRED_TOKEN("토큰의 유효기간이 만료되었습니다"),
  INVALID_TOKEN("잘못된 토큰을 전달하였습니다"),
  AUTHORIZATION_HEADER_REQUIRED("Authorization Header가 없습니다"),
  INVALID_JWT_SIGNATURE("유효하지 않은 Secret Key를 사용중입니다"),
  INVALID_REQUEST_BODY("정확한 값을 Request Body에 담아서 요청하시기 바랍니다"),
  CHATROOM_NOT_FOUND("채팅방을 찾을 수 없습니다"),
  USER_NOT_FOUND("사용자를 찾을 수 없습니다"),
  BOARD_NOT_FOUND("게시글을 찾을 수 없습니다"),
  CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다"),
  TAG_NOT_FOUND("사용자 유형을 찾을 수 없습니다"),
  ADDRESS_NOT_FOUND("주소를 찾을 수 없습니다"),
  WRONG_CHATROOM_HOST("이 사용자는 채팅방의 호스트가 아닙니다"),
  WRONG_CHATROOM_GUEST("이 사용자는 채팅방의 게스트가 아닙니다"),
  CHATROOM_ALREADY_CREATED("채팅방이 이미 개설되어 있습니다"),
  ALREADY_APPROVED("이 사용자는 이미 승인받은 상태입니다"),
  BOARD_POST_SUCCESS("글이 정상적으로 업로드 되었습니다"),
  BOARD_DELETE_SUCCESS("글이 정상적으로 삭제 되었습니다"),
  BOARD_CONTENT_SUCCESS("글 내용 조회 성공"),
  BOOKMARK_POST_SUCCESS("북마크가 정상적으로 추가 되었습니다"),
  BOOKMARK_DELETE_SUCCESS("북마크가 정상적으로 삭제 되었습니다"),
  REPORT_SUCCESS("신고가 정상적으로 접수되었습니다."),
  HIDDEN_SUCCESS("게시물이 피드에서 숨김처리 되었습니다."),
  INACTIVE_USER_FAIL("회원 가입이 필요합니다."),
  SUSPENDED_USER_FAIL("신고로 정지된 사용자입니다."),
  INTERNAL_SERVER_FAIL("서버 내부 오류"),
	INVALID_METHOD("메소드 유형이 잘못 되었습니다."),
  INVALID_REQUEST_PARAM("정확한 값을 Request Parameter에 담아서 요청하시기 바랍니다");

  private final String message;

  ResponseType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
