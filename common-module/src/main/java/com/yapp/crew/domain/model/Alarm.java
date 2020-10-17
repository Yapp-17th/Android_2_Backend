package com.yapp.crew.domain.model;

import com.yapp.crew.domain.type.AlarmType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  @Enumerated(value = EnumType.ORDINAL)
  private AlarmType type;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  private String content;

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
