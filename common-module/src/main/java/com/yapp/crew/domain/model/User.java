package com.yapp.crew.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yapp.crew.domain.status.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false, length = 100)
  private String username;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false, unique = true, length = 100)
  private String nickname;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false, length = 255)
  private String intro;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(name = "access_token", nullable = false, unique = true, length = 100)
  private String accessToken;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(name = "oauth_id", nullable = false, unique = true, length = 100)
  private String oauthId;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(name = "report_points", nullable = false)
  private Integer reportPoints = 0;

  @Setter(value = AccessLevel.PRIVATE)
  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private UserStatus status = UserStatus.ACTIVE;

  @JsonBackReference
  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Address address;

  @JsonBackReference
  @Setter(value = AccessLevel.PRIVATE)
  @OneToMany(mappedBy = "user")
  private Set<UserExercise> userExercise = new HashSet<>();

	@OneToMany(mappedBy = "board")
	private Set<AppliedUser> appliedUsers = new HashSet<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Board> boards = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private Set<BookMark> userBookmark = new HashSet<>();

  @OneToMany(mappedBy = "user")
  private Set<HiddenBoard> userHiddenBoard = new HashSet<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY)
  private List<Report> reportList = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "reported", fetch = FetchType.LAZY)
  private List<Report> reportedList = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "host", fetch = FetchType.LAZY)
  private List<ChatRoom> hostList = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
  private List<ChatRoom> guestList = new ArrayList<>();

  public void addBoard(Board board) {
    board.setUser(this);
    this.boards.add(board);
  }

  public void addReport(Report report) {
    report.setReporter(this);
    this.reportList.add(report);
  }

  public void addReported(Report reported) {
    reported.setReported(this);
    this.reportedList.add(reported);
  }

  public void addChatRoomHost(ChatRoom chatRoom) {
    if (hostList.contains(chatRoom)) {
      return;
    }
    hostList.add(chatRoom);
  }

  public void addChatRoomGuest(ChatRoom chatRoom) {
    if (guestList.contains(chatRoom)) {
      return;
    }
    guestList.add(chatRoom);
  }

  public long calculateLikes(List<Evaluation> evaluations) {
    return evaluations.stream()
        .filter(Evaluation::getIsLike)
        .count();
  }

  public long calculateDislikes(List<Evaluation> evaluations) {
    return evaluations.stream()
        .filter(Evaluation::getIsDislike)
        .count();
  }

  public static UserBuilder getBuilder() {
    return new UserBuilder();
  }

  public static class UserBuilder {

    private String username;
    private String nickname;
    private String email;
    private String accessToken;
    private String oauthId;
    private Address address;
    private String intro;

    public UserBuilder withUsername(String username) {
      this.username = username;
      return this;
    }

    public UserBuilder withNickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    public UserBuilder withEmail(String email) {
      this.email = email;
      return this;
    }

    public UserBuilder withAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    public UserBuilder withOauthId(String oauthId) {
      this.oauthId = oauthId;
      return this;
    }

    public UserBuilder withIntro(String intro) {
      this.intro = intro;
      return this;
    }

    public UserBuilder withAddress(Address address) {
      this.address = address;
      return this;
    }

    public User build() {
      User user = new User();
      user.setUsername(username);
      user.setNickname(nickname);
      user.setEmail(email);
      user.setAccessToken(accessToken);
      user.setAddress(address);
      user.setIntro(intro);
      user.setOauthId(oauthId);
      return user;
    }
  }
}
