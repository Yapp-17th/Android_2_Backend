package com.yapp.crew.model;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.UserStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfo {

	private Long userId = -1L;
	private String userName = "(알수없음)";
	private String nickName = "(알수없음)";
	private String email = "";
	private String intro = "";
	private List<CategoryCode> category = new ArrayList<>();
	private CityCode city;

	public static UserInfo build(User user) {
		UserInfo userInfo = new UserInfo();

		if (user.getStatus() == UserStatus.ACTIVE || user.getStatus() == UserStatus.SUSPENDED) {
			userInfo.userId = user.getId();
			userInfo.userName = user.getUsername();
			userInfo.nickName = user.getNickname();
			userInfo.email = user.getEmail();
			userInfo.intro = user.getIntro();
			userInfo.category.addAll(user.getUserExercise().stream().map(exercise -> CategoryCode.build(exercise.getCategory())).collect(Collectors.toSet()));
			userInfo.city = CityCode.build(user.getAddress());
		}

		return userInfo;
	}
}
