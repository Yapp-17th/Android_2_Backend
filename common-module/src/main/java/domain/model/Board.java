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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import domain.status.GroupStatus;

@Entity
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  private Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  private Tag tag;

  private String title;
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime startAt;

  @Enumerated(value = EnumType.ORDINAL)
  private GroupStatus status = GroupStatus.RECRUITING;

  @NotNull
  private Integer recruitCount = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  private User bookMarkUser;
}
