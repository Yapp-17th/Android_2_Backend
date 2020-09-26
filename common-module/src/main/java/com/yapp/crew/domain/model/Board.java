package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.GroupStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Board extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  private Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  private Tag tag;

  @Enumerated(value = EnumType.ORDINAL)
  private GroupStatus status = GroupStatus.RECRUITING;

  @Column(name = "recruit_count", nullable = false)
  private Integer recruitCount = 0;

  @Column(name = "starts_at", nullable = false)
  private LocalDateTime startsAt;

  public Board() {

  }
}
