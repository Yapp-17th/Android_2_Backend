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

	private int status;
	private boolean success;
	private String message;
	private UserInfo data;

	public static UserEditProfileResponseDtoBuilder getBuilder() {
		return new UserEditProfileResponseDtoBuilder();
	}

	public static class UserEditProfileResponseDtoBuilder {
		private int status = HttpStatus.OK.value();
		private boolean success = true;
		private String message = "회원 수정용 프로필 조회 완료";
		private UserInfo data;

		public UserEditProfileResponseDtoBuilder withStatus(int status) {
			this.status = status;
			return this;
		}

		public UserEditProfileResponseDtoBuilder withSuccess(boolean success) {
			this.success = success;
			return this;
		}

		public UserEditProfileResponseDtoBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public UserEditProfileResponseDtoBuilder withData(UserInfo data) {
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
