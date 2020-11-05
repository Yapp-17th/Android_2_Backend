package com.yapp.crew.domain.status;

public enum BoardStatus {
  NORMAL(0),
  CANCELED(1);

  private final int code;

  BoardStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}
