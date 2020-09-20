package domain.status;

public enum Exercise {
  RUNNNING("러닝"),
  HIKING("등산"),
  BASKETBALL("농구"),
  SOCCER("축구"),
  BICYCLE("자전거"),
  CLIMBING("클라이밍"),
  OTHERS("기타");

  private String name;

  Exercise(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
