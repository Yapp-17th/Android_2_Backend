package domain.model;

import java.time.LocalDateTime;
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
import org.hibernate.annotations.CreationTimestamp;
import domain.status.Exercise;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
  private List<User> user = new ArrayList<>();;

  @Enumerated(value = EnumType.STRING)
  private Exercise name;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
  private List<Board> boards = new ArrayList<>();
}
