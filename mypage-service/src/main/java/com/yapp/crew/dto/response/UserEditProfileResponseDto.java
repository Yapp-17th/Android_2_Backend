package com.yapp.crew.dto.response;

import com.yapp.crew.model.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class UserEditProfileResponseDto {

	private Integer status = HttpStatus.OK.value();
	private Boolean success = true;
	private String message = "회원 수정용 프로필 조회 완료";
	private UserInfo data;

	public static UserEditProfileResponseDto build(UserInfo userInfo) {
		UserEditProfileResponseDto userEditProfileResponseDto = new UserEditProfileResponseDto();
		userEditProfileResponseDto.data = userInfo;

		return userEditProfileResponseDto;
	}
}
