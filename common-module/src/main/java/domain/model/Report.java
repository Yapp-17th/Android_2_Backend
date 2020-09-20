package domain.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import domain.status.ReportType;

@Entity
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User reporter;

  @ManyToOne(fetch = FetchType.LAZY)
  private User reported;

  @Enumerated(value = EnumType.ORDINAL)
  private ReportType type;
  private String content;
}
