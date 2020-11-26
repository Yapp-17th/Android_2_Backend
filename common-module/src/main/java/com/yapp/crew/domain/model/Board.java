package com.yapp.crew.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.status.BoardStatus;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false)
	private String title;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false)
	private String content;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false)
	private String place;

	@JsonBackReference
	@Setter(value = AccessLevel.PROTECTED)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@JsonBackReference
	@Setter(value = AccessLevel.PRIVATE)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@JsonBackReference
	@Setter(value = AccessLevel.PRIVATE)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Address address;

	@JsonBackReference
	@Setter(value = AccessLevel.PRIVATE)
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tag tag;

	@Column(nullable = false)
	@Enumerated(value = EnumType.ORDINAL)
	private BoardStatus status = BoardStatus.RECRUITING;

	@Column(name = "recruit_count", nullable = false)
	@Setter(value = AccessLevel.PRIVATE)
	private Integer recruitCount = 0;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(name = "starts_at", nullable = false)
	private LocalDateTime startsAt;

	@OneToMany(mappedBy = "board")
	private Set<AppliedUser> appliedUsers = new HashSet<>();

	@OneToMany(mappedBy = "board")
	private Set<BookMark> bookMarkUser = new HashSet<>();

	@OneToMany(mappedBy = "board")
	private Set<HiddenBoard> hiddenBoardUser = new HashSet<>();

	@OneToMany(mappedBy = "board")
	private Set<Evaluation> evaluations = new HashSet<>();

	public void addBookMark(BookMark bookMark) {
		bookMarkUser.add(bookMark);
	}

	public void addAppliedUser(AppliedUser appliedUser) {
		if (appliedUsers.contains(appliedUser)) {
			return;
		}
		appliedUsers.add(appliedUser);
	}

	public void deleteBoard() {
		this.status = BoardStatus.CANCELED;
	}

	public void addHiddenBoard(HiddenBoard hiddenBoard) {
		this.hiddenBoardUser.add(hiddenBoard);
	}

	public int getRemainRecruitNumber() {
		return this.recruitCount - getApprovedCount();
	}

	public void finishBoard() {
		this.status = BoardStatus.FINISHED;
	}

	public void completeRecruiting() {
		this.status = BoardStatus.COMPLETE;
	}

	public String showBoardTimeComparedToNow() {
		Calendar calendar = Calendar.getInstance();
		Date now = java.sql.Timestamp.valueOf(LocalDateTime.now());
		Date startDate = java.sql.Timestamp.valueOf(startsAt);

		if (status == BoardStatus.RECRUITING || status == BoardStatus.COMPLETE) {
			return "[D-" + calculateLeftDays(now, startDate) + "]";
		}

		calendar.setTime(startDate);
		return "[" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DATE) + "]";
	}

	private int calculateLeftDays(Date now, Date end) {
		long diffInMillies = Math.abs(now.getTime() - end.getTime());
		return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public int getApprovedCount() {
		return (int) appliedUsers.stream()
				.filter(appliedUser -> appliedUser.getStatus() == AppliedStatus.APPROVED).count();
	}

	public static BoardBuilder getBuilder() {
		return new BoardBuilder();
	}

	public static class BoardBuilder {

		private String title;
		private String content;
		private String place;
		private int recruitCount;
		private User user;
		private Category category;
		private Address address;
		private Tag tag;
		private LocalDateTime startsAt;

		public BoardBuilder withTitle(String title) {
			this.title = title;
			return this;
		}

		public BoardBuilder withContent(String content) {
			this.content = content;
			return this;
		}

		public BoardBuilder withPlace(String place) {
			this.place = place;
			return this;
		}

		public BoardBuilder withRecruitCount(int recruitCount) {
			this.recruitCount = recruitCount;
			return this;
		}

		public BoardBuilder withUser(User user) {
			this.user = user;
			return this;
		}

		public BoardBuilder withCategory(Category category) {
			this.category = category;
			return this;
		}

		public BoardBuilder withAddress(Address address) {
			this.address = address;
			return this;
		}

		public BoardBuilder withTag(Tag tag) {
			this.tag = tag;
			return this;
		}

		public BoardBuilder withStartsAt(LocalDateTime startsAt) {
			this.startsAt = startsAt;
			return this;
		}

		public Board build() {
			Board board = new Board();
			build(board);
			return board;
		}

		public Board build(Board board) {
			board.setTitle(title);
			board.setUser(user);
			board.setCategory(category);
			board.setAddress(address);
			board.setTag(tag);
			board.setStartsAt(startsAt);
			board.setContent(content);
			board.setPlace(place);
			board.setRecruitCount(recruitCount);
			return board;
		}
	}
}
