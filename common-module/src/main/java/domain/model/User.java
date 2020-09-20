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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import domain.status.UserStatus;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userName;
  private String email;
  private String accessToken;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private String nickName;

  @NotNull
  private Integer likes = 0;
  @NotNull
  private Integer dislikes = 0;
  @NotNull
  private Integer reportPoints = 0;

  @Enumerated(value = EnumType.ORDINAL)
  private UserStatus status = UserStatus.ACTIVE;

  @ManyToOne(fetch = FetchType.LAZY)
  private Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Board> boards = new ArrayList<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Board> bookMarks = new ArrayList<>();

  @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY)
  private List<Report> reportList = new ArrayList<>();

  @OneToMany(mappedBy = "reported", fetch = FetchType.LAZY)
  private List<Report> reportedList = new ArrayList<>();

  @OneToMany(mappedBy = "host", fetch = FetchType.LAZY)
  private List<ChatRoom> hostList = new ArrayList<>();

  @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
  private List<ChatRoom> guestList = new ArrayList<>();
}
