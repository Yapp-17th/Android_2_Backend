package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.UserStatus;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

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
  @Column(name = "access_token", nullable = false, unique = true, length = 100)
  private String accessToken;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  private Integer likes = 0;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  private Integer dislikes = 0;

  @Setter(value = AccessLevel.PRIVATE)
  @Column(name = "report_points", nullable = false)
  private Integer reportPoints = 0;

  @Setter(value = AccessLevel.PRIVATE)
  @Enumerated(value = EnumType.ORDINAL)
  @Column(nullable = false)
  private UserStatus status = UserStatus.ACTIVE;

  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Address address;

  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Board> boards = new ArrayList<>();

  @Transient
  private List<Board> bookmarks = new ArrayList<>();

  @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY)
  private List<Report> reportList = new ArrayList<>();

  @OneToMany(mappedBy = "reported", fetch = FetchType.LAZY)
  private List<Report> reportedList = new ArrayList<>();

  @OneToMany(mappedBy = "host", fetch = FetchType.LAZY)
  private List<ChatRoom> hostList = new ArrayList<>();

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
    chatRoom.setHost(this);
    this.hostList.add(chatRoom);
  }

  public void addChatRoomGuest(ChatRoom chatRoom) {
    chatRoom.setGuest(this);
    this.guestList.add(chatRoom);
  }

  public static UserBuilder getBuilder() {
    return new UserBuilder();
  }

  public static class UserBuilder {

    private String username;
    private String nickname;
    private String email;
    private String accessToken;
    private Address address;
    private Category category;

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

    public UserBuilder withAddress(Address address) {
      this.address = address;
      return this;
    }

    public UserBuilder withCategory(Category category) {
      this.category = category;
      return this;
    }

    public User build() {
      User user = new User();
      user.setUsername(username);
      user.setNickname(nickname);
      user.setEmail(email);
      user.setAccessToken(accessToken);
      user.setAddress(address);
      user.setCategory(category);
      return user;
    }
  }
}
