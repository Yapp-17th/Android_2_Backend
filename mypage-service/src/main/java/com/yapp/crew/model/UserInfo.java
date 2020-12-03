package com.yapp.crew.model;

import com.yapp.crew.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfo {

	private Long userId;
	private String userName;
	private String nickName;
	private String email;
	private String intro;
	private List<String> category = new ArrayList<>();
	private String city;

	public static UserInfo build(User user){
		UserInfo userInfo = new UserInfo();
		userInfo.userId = user.getId();
		userInfo.userName = user.getUsername();
		userInfo.nickName = user.getNickname();
		userInfo.email = user.getEmail();
		userInfo.intro = user.getIntro();
		userInfo.category.addAll(user.getUserExercise().stream().map(exercise -> exercise.getCategory().getExercise().getName()).collect(Collectors.toSet()));
		userInfo.city = user.getAddress().getCity().getName();

		return userInfo;
	}
}
