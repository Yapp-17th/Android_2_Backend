package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.UserStatus;

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
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String email;

  @Column(name = "access_token", nullable = false, unique = true)
  private String accessToken;

  private Integer likes = 0;

  private Integer dislikes = 0;

  @Column(name = "report_points")
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
  private List<Board> bookmarks = new ArrayList<>();

  @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY)
  private List<Report> reportList = new ArrayList<>();

  @OneToMany(mappedBy = "reported", fetch = FetchType.LAZY)
  private List<Report> reportedList = new ArrayList<>();

  @OneToMany(mappedBy = "host", fetch = FetchType.LAZY)
  private List<ChatRoom> hostList = new ArrayList<>();

  @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
  private List<ChatRoom> guestList = new ArrayList<>();

  protected User() {

  }
}
