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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.CreationTimestamp;
import domain.status.Exercise;

@Entity
public class Category {

  @Id
  @GeneratedValue
  private long id;

  @OneToOne(fetch = FetchType.LAZY)
  private User user;

  @Enumerated(value = EnumType.STRING)
  private Exercise name;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Board> members = new ArrayList<>();
}
