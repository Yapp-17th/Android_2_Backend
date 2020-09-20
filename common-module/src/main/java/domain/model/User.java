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
import domain.status.UserStatus;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String userName;
  private String email;
  private String accessToken;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private String nickName;
  private int likes = 0;
  private int dislikes = 0;
  private int reportPoints = 0;

  @Enumerated(value = EnumType.ORDINAL)
  private UserStatus status = UserStatus.ACTIVE;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
  private Address address;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
  private Category categoty;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
  private Tag tag;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Board> members = new ArrayList<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<BookMark> bookMarks = new ArrayList<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Message> messages = new ArrayList<>();

  @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY)
  private List<Report> reportList = new ArrayList<>();

  @OneToMany(mappedBy = "reported", fetch = FetchType.LAZY)
  private List<Report> reportedList = new ArrayList<>();

  @OneToMany(mappedBy = "host", fetch = FetchType.LAZY)
  private List<ChatRoom> hostList = new ArrayList<>();

  @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
  private List<ChatRoom> guestList = new ArrayList<>();
}
