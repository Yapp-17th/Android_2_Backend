package domain.status;

public enum UserTag {
  TIGHT_USER("빡겜러"),
  FUN_USER("즐겜러"),
  NOTALK_USER("no-talk");

  private String name;

  UserTag(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
