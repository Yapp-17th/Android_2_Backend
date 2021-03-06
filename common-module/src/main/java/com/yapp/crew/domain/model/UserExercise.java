package com.yapp.crew.domain.model;

import javax.persistence.Entity;
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
public class UserExercise extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@Setter(value = AccessLevel.PRIVATE)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	@Setter(value = AccessLevel.PRIVATE)
	private Category category;

	public static UserExerciseBuilder getBuilder() {
		return new UserExerciseBuilder();
	}

	public static class UserExerciseBuilder {
		private User user;
		private Category category;

		public UserExerciseBuilder withUser(User user) {
			this.user = user;
			return this;
		}

		public UserExerciseBuilder withCategory(Category category) {
			this.category = category;
			return this;
		}

		public UserExercise build() {
			UserExercise userExercise = new UserExercise();
			userExercise.setUser(user);
			userExercise.setCategory(category);
			return userExercise;
		}
	}
}
