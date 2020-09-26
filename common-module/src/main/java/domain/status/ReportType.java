package domain.status;

public enum ReportType {
  NON_MANNERS(1, "욕설 및 비매너 행위"),
  NON_PARTICIPATION(2, "미참여 및 연락두절"),
  EAT_AND_RUN(3, "대관비, 음료 관련 정산 중 잠수"),
  BOTHERING(4, "원치 않는 지속적인 연락 및 강요"),
  OTHERS(0, "기타");

  private final int code;
  private final String name;

  ReportType(int code, String name) {
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
