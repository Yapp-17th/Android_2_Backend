package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.ReportType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Report extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User reporter;

  @ManyToOne(fetch = FetchType.LAZY)
  private User reported;

  @Enumerated(value = EnumType.ORDINAL)
  private ReportType type;

  @Column(nullable = false)
  private String content;

  public Report() {

  }
}
