package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
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
public class User extends BaseEntity {

  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  private String username;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  private String nickname;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Column(nullable = false)
  private String email;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Column(name = "access_token", nullable = false, unique = true)
  private String accessToken;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  private Integer likes = 0;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  private Integer dislikes = 0;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Column(name = "report_points")
  private Integer reportPoints = 0;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @Enumerated(value = EnumType.ORDINAL)
  private UserStatus status = UserStatus.ACTIVE;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Address address;

  @Getter
  @Setter(value = AccessLevel.PRIVATE)
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @Getter
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Board> boards = new ArrayList<>();

  @Getter
  @Transient
  private List<Board> bookmarks = new ArrayList<>();

  @Getter
  @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY)
  private List<Report> reportList = new ArrayList<>();

  @Getter
  @OneToMany(mappedBy = "reported", fetch = FetchType.LAZY)
  private List<Report> reportedList = new ArrayList<>();

  @Getter
  @OneToMany(mappedBy = "host", fetch = FetchType.LAZY)
  private List<ChatRoom> hostList = new ArrayList<>();

  @Getter
  @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
  private List<ChatRoom> guestList = new ArrayList<>();

  protected User() {

  }

  // TODO: add, delete, increase, decrease, update function

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
