package com.yapp.crew.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileInfo {

	private long userId;

	@JsonProperty(value = "isMine")
	private boolean isMine;

	private String nickName;

	private int like;

	private int dislike;

	private String intro;

	private String city;

	private List<String> category;

	public static UserProfileInfo emptyBody() {
		return UserProfileInfo.getBuilder().build();
	}

	public static UserProfileInfoBuilder getBuilder() {
		return new UserProfileInfoBuilder();
	}

	public static class UserProfileInfoBuilder {
		private long userId = -1L;
		private boolean isMine = false;
		private String nickName = "(알수없음)";
		private int like = 0;
		private int dislike = 0;
		private String intro = "";
		private String city = "";
		private List<String> category = Collections.emptyList();

		public UserProfileInfoBuilder withUserId(long userId) {
			this.userId = userId;
			return this;
		}

		public UserProfileInfoBuilder withIsMine(boolean isMine) {
			this.isMine = isMine;
			return this;
		}

		public UserProfileInfoBuilder withNickName(String nickName) {
			this.nickName = nickName;
			return this;
		}

		public UserProfileInfoBuilder withLike(int like) {
			this.like = like;
			return this;
		}

		public UserProfileInfoBuilder withDislike(int dislike) {
			this.dislike = dislike;
			return this;
		}

		public UserProfileInfoBuilder withIntro(String intro) {
			this.intro = intro;
			return this;
		}

		public UserProfileInfoBuilder withCity(String city) {
			this.city = city;
			return this;
		}

		public UserProfileInfoBuilder withCategory(List<String> category) {
			this.category = category;
			return this;
		}

		public UserProfileInfo build() {
			UserProfileInfo userProfileInfo = new UserProfileInfo();
			userProfileInfo.setUserId(userId);
			userProfileInfo.setMine(isMine);
			userProfileInfo.setNickName(nickName);
			userProfileInfo.setLike(like);
			userProfileInfo.setDislike(dislike);
			userProfileInfo.setIntro(intro);
			userProfileInfo.setCity(city);
			userProfileInfo.setCategory(category);
			return userProfileInfo;
		}
	}

	public static UserProfileInfo build(User user, boolean isMine, List<Evaluation> evaluations) {
		if (user.isValidUser()) {
			return UserProfileInfo.getBuilder()
					.withUserId(user.getId())
					.withIsMine(isMine)
					.withNickName(user.getNickname())
					.withLike(Math.toIntExact(user.calculateLikes(evaluations)))
					.withDislike(Math.toIntExact(user.calculateDislikes(evaluations)))
					.withIntro(user.getIntro())
					.withCity(user.getAddress().getCity().getName())
					.withCategory(user.getUserExercise().stream().map(exercise -> exercise.getCategory().getExercise().getName()).collect(Collectors.toList()))
					.build();
		}
		return UserProfileInfo.getBuilder().build();
	}
}
