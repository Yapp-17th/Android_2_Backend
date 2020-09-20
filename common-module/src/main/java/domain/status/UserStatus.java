package domain.status;

public enum UserStatus {
  ACTIVE(1),
  INACTIVE(0);

  private int code;

  UserStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}
