package com.yapp.crew.domain.status;

public enum GroupStatus {
  RECRUITING(0, "모집 중"),
  COMPLETE(1, "모집 완료"),
  FINISHED(2, "활동 종료"),
  CANCELED(3, "모임 취소");

  private final int code;
  private final String name;

  GroupStatus(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public int getCode() {
    return this.code;
  }

  public String getName() {
    return this.name;
  }
}
