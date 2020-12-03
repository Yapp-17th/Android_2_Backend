package com.yapp.crew.dto.response;

import com.yapp.crew.model.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
public class UserEditProfileResponseDto {

	private Integer status;
	private Boolean success;
	private String message;
	private UserInfo data;

	public static UserEditProfileResponseDtoBuidler getBuilder() {
		return new UserEditProfileResponseDtoBuidler();
	}

	public static class UserEditProfileResponseDtoBuidler {

		private Integer status = HttpStatus.OK.value();
		private Boolean success = true;
		private String message = "회원 수정용 프로필 조회 완료";
		private UserInfo data;

		public UserEditProfileResponseDtoBuidler withStatus(Integer status) {
			this.status = status;
			return this;
		}

		public UserEditProfileResponseDtoBuidler withSuccess(Boolean success) {
			this.success = success;
			return this;
		}

		public UserEditProfileResponseDtoBuidler withMessage(String message) {
			this.message = message;
			return this;
		}

		public UserEditProfileResponseDtoBuidler withData(UserInfo data) {
			this.data = data;
			return this;
		}

		public UserEditProfileResponseDto build() {
			UserEditProfileResponseDto userEditProfileResponseDto = new UserEditProfileResponseDto();
			userEditProfileResponseDto.setStatus(status);
			userEditProfileResponseDto.setSuccess(success);
			userEditProfileResponseDto.setMessage(message);
			userEditProfileResponseDto.setData(data);
			return userEditProfileResponseDto;
		}
	}

	public static UserEditProfileResponseDto build(UserInfo userInfo) {
		return UserEditProfileResponseDto.getBuilder().withData(userInfo).build();
	}
}
