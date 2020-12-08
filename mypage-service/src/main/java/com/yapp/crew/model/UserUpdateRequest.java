package com.yapp.crew.model;

import com.yapp.crew.dto.request.UserUpdateRequestDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

	private String userName;
	private String nickName;
	private String email;
	private List<Long> category;
	private long address;
	private String intro;

	public static UserUpdateRequest build(UserUpdateRequestDto userUpdateRequestDto) {
		UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
		userUpdateRequest.userName = userUpdateRequestDto.getUserName();
		userUpdateRequest.nickName = userUpdateRequestDto.getNickName();
		userUpdateRequest.email = userUpdateRequestDto.getEmail();
		userUpdateRequest.category = userUpdateRequestDto.getCategory();
		userUpdateRequest.address = userUpdateRequestDto.getAddress();
		userUpdateRequest.intro = userUpdateRequestDto.getIntro();

		return userUpdateRequest;
	}
}
