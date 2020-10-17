package com.yapp.crew.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yapp.crew.domain.type.ReportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference
  @Setter(value = AccessLevel.PROTECTED)
  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private User reporter;

  @JsonBackReference
  @Setter(value = AccessLevel.PROTECTED)
  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private User reported;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  @Enumerated(value = EnumType.ORDINAL)
  private ReportType type;

  @Setter(value = AccessLevel.PRIVATE)
  private String content;

  public static ReportBuilder getBuilder() {
    return new ReportBuilder();
  }

  public static class ReportBuilder {
    private User reporter;
    private User reported;
    private ReportType type;
    private String content;

    public ReportBuilder withReporter(User reporter) {
      this.reporter = reporter;
      return this;
    }

    public ReportBuilder withReported(User reported) {
      this.reported = reported;
      return this;
    }

    public ReportBuilder withType(ReportType type) {
      this.type = type;
      return this;
    }

    public ReportBuilder withContent(String content) {
      this.content = content;
      return this;
    }

    public Report build() {
      Report report = new Report();
      report.setReporter(reporter);
      report.setReported(reported);
      report.setType(type);
      report.setContent(content);
      return report;
    }
  }
}
