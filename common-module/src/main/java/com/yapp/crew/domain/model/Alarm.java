package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.AlarmType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Alarm extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.ORDINAL)
  private AlarmType type;

  private String content;

  protected Alarm() {

  }
}
