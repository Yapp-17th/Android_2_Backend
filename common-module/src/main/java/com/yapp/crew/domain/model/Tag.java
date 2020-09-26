package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.UserTag;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private UserTag name;

  @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
  private List<Board> boards = new ArrayList<>();

  public Tag() {

  }
}
