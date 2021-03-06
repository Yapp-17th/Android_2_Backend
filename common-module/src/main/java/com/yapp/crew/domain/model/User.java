package com.yapp.crew.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yapp.crew.domain.status.UserStatus;
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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
		@Index(name = "nickname", columnList = "nickname", unique = true),
		@Index(name = "email", columnList = "email", unique = true),
		@Index(name = "access_token", columnList = "access_token", unique = true),
		@Index(name = "oauth_id", columnList = "oauth_id", unique = true)
})
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false, length = 30)
	private String username;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false, length = 30)
	private String nickname;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false, length = 30)
	private String email;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false)
	private String intro;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "access_token", nullable = false, length = 100)
	private String accessToken;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "oauth_id", nullable = false, length = 100)
	private String oauthId;

	@Setter(value = AccessLevel.PRIVATE)
	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status = UserStatus.ACTIVE;

	@JsonBackReference
	@Setter(value = AccessLevel.PRIVATE)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Address address;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "suspended_day", nullable = false)
	private int suspendedDay;

	@JsonManagedReference
	@Setter(value = AccessLevel.PRIVATE)
	@OneToMany(mappedBy = "user")
	private Set<UserExercise> userExercise = new HashSet<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user")
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

	public int calculateReportedPoint() {
		return reportedList.size();
	}

	public void addAppliedUser(AppliedUser appliedUser) {
		if (appliedUsers.contains(appliedUser)) {
			return;
		}
		appliedUsers.add(appliedUser);
	}

	public void addBookMark(BookMark bookMark) {
		userBookmark.add(bookMark);
	}

	public void addHiddenBoard(HiddenBoard hiddenBoard) {
		this.userHiddenBoard.add(hiddenBoard);
	}

	public void addReport(Report report) {
		this.reportList.add(report);
	}

	public void addReported(Report reported) {
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
		if (evaluations == null) {
			return 0L;
		}
		return evaluations.stream()
				.filter(Evaluation::isLike)
				.count();
	}

	public long calculateDislikes(List<Evaluation> evaluations) {
		if (evaluations == null) {
			return 0L;
		}
		return evaluations.stream()
				.filter(Evaluation::isDislike)
				.count();
	}

	public void setUserStatusInActive() {
		this.setStatus(UserStatus.INACTIVE);
	}

	public void setUserStatusActive() {
		this.setStatus(UserStatus.ACTIVE);
	}

	public void setUserStatusSuspended() {
		this.setStatus(UserStatus.SUSPENDED);
	}

	public void increaseSuspendedDays() {
		this.suspendedDay += 1;
	}

	public void resetSuspendedDays() {
		this.suspendedDay = 0;
	}

	public void setUserStatusForbidden() {
		this.setStatus(UserStatus.FORBIDDEN);
	}

	public boolean isValidUser() {
		return this.getStatus() == UserStatus.ACTIVE || this.getStatus() == UserStatus.SUSPENDED;
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

		public User build(User user) {
			user.setUsername(username);
			user.setNickname(nickname);
			user.setEmail(email);
			user.setAddress(address);
			user.setIntro(intro);
			return user;
		}
	}
}
