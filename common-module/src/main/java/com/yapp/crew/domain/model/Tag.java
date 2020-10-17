package com.yapp.crew.domain.model;

import com.yapp.crew.domain.type.UserTag;
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
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter(value = AccessLevel.PRIVATE)
  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private UserTag name;

  @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
  private List<Board> boards = new ArrayList<>();
}
