package com.yapp.crew.model;

import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileInfo {

	private boolean isMine;
	private String userName;
	private int like;
	private int dislike;
	private String intro;
	private List<String> category = new ArrayList<>();
	private String city;

	public static UserProfileInfo build(User user, boolean isMine, List<Evaluation> evaluations) {
		UserProfileInfo userProfileInfo = new UserProfileInfo();
		userProfileInfo.isMine = isMine;
		userProfileInfo.userName = user.getNickname();
		userProfileInfo.like = Math.toIntExact(user.calculateLikes(evaluations));
		userProfileInfo.dislike = Math.toIntExact(user.calculateDislikes(evaluations));
		userProfileInfo.intro = user.getIntro();
		userProfileInfo.category.addAll(user.getUserExercise().stream().map(exerciese -> exerciese.getCategory().getExercise().getName()).collect(Collectors.toSet()));
		userProfileInfo.city = user.getAddress().getCity().getName();

		return userProfileInfo;
	}
}
