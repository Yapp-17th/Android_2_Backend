package com.yapp.crew.domain.model;

import com.yapp.crew.domain.type.ExerciseType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter(value = AccessLevel.PRIVATE)
  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private ExerciseType name;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<User> users = new ArrayList<>();;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Board> boards = new ArrayList<>();
}
