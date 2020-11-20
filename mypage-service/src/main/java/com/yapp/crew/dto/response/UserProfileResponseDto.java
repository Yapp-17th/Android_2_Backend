package com.yapp.crew.dto.response;

import com.yapp.crew.model.UserProfileInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class UserProfileResponseDto {

	private Integer status = HttpStatus.OK.value();
	private Boolean success = true;
	private String message = "회원 프로필 조회 완료";
	private UserProfileInfo data;

	public static UserProfileResponseDto build(UserProfileInfo userProfileInfo) {
		UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
		userProfileResponseDto.data = userProfileInfo;

		return userProfileResponseDto;
	}
}
