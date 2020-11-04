package com.yapp.crew.domain.model;

import com.yapp.crew.domain.status.AppliedStatus;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppliedUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter(value = AccessLevel.PRIVATE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Setter(value = AccessLevel.PRIVATE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@Setter(value = AccessLevel.PRIVATE)
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private AppliedStatus status = AppliedStatus.PENDING;

	public static AppliedUser buildAppliedUser(User user, Board board, AppliedStatus status) {
		return AppliedUser.getBuilder()
						.withUser(user)
						.withBoard(board)
						.withStatus(status)
						.build();
	}

	public void approveUser() {
		setStatus(AppliedStatus.APPROVED);
	}

	public static AppliedUserBuilder getBuilder() {
		return new AppliedUserBuilder();
	}

	public static class AppliedUserBuilder {
		private User user;
		private Board board;
		private AppliedStatus status;

		public AppliedUserBuilder withUser(User user) {
			this.user = user;
			return this;
		}

		public AppliedUserBuilder withBoard(Board board) {
			this.board = board;
			return this;
		}

		public AppliedUserBuilder withStatus(AppliedStatus status) {
			this.status = status;
			return this;
		}

		public AppliedUser build() {
			AppliedUser appliedUser = new AppliedUser();
			appliedUser.setUser(user);
			appliedUser.setBoard(board);
			appliedUser.setStatus(status);
			return appliedUser;
		}
	}
}
