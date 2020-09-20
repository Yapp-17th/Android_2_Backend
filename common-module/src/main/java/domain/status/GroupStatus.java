package domain.status;

public enum GroupStatus {
  RECRUITING(0, "모집중"),
  COMPLETE(1, "모집 완료"),
  CANCELED(2, "모임 취소");

  private int code;
  private String name;

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
