package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.AlarmType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Alarm extends BaseEntity {

  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Enumerated(value = EnumType.ORDINAL)
  private AlarmType type;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  private String content;

  protected Alarm() {

  }

  public static AlarmBuilder getBuilder() {
    return new AlarmBuilder();
  }

  public static class AlarmBuilder {
    private AlarmType type;
    private String content;

    public AlarmBuilder withType(AlarmType type) {
      this.type = type;
      return this;
    }

    public AlarmBuilder withContent(String content) {
      this.content = content;
      return this;
    }

    public Alarm build() {
      Alarm alarm = new Alarm();
      alarm.setType(type);
      alarm.setContent(content);
      return alarm;
    }
  }
}
