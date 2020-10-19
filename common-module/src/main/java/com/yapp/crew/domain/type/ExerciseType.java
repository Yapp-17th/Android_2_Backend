package com.yapp.crew.domain.type;

public enum ExerciseType {
  SOCCER("축구"),
  BASKETBALL("농구"),
  TENNIS("테니스"),
  RUNNING("러닝"),
  HIKING("등산"),
  BICYCLE("자전거"),
  CLIMBING("클라이밍"),
  VOLLYBALL("배구"),
  BASEBALL("야구"),
  FUTSAL("풋살"),
  BADMINTON("배드민턴"),
  RUGBY("럭비"),
  BOXING("복싱"),
  ICE_HOKEY("아이스하키"),
  GOLF("골프"),
  OTHERS("기타");

  private final String name;

  ExerciseType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
