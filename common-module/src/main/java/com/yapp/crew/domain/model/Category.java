package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.ExerciseType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
public class Category extends BaseEntity {

  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Enumerated(value = EnumType.STRING)
  private ExerciseType name;

  @Getter
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<User> users = new ArrayList<>();;

  @Getter
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Board> boards = new ArrayList<>();

  protected Category() {

  }
}
