package domain.status;

public enum AlarmType {
  MESSAGE(0, "채팅 알람"),
  REMIND(1, "모임이 D-1 남았습니다."),
  SCHEDULE_CHANGE(2, "일정이 변동되었습니다."),
  JOIN_GROUP(3, "모임 참여(게스트에게 알람)"),
  REQUEST_GROUP(4, "모임 신청(호스트에게 알람)"),
  EVALUATE_GROUP(5, "모임을 평가해주세요");

  private int code;
  private String name;

  AlarmType(int code, String name) {
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
