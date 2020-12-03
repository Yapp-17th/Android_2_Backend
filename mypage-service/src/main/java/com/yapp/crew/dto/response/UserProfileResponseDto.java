package com.yapp.crew.dto.response;

import com.yapp.crew.model.UserProfileInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileResponseDto {

	private Integer status;
	private Boolean success;
	private String message;
	private UserProfileInfo data;

	public static UserProfileResponseDtoBuilder getBuilder() {
		return new UserProfileResponseDtoBuilder();
	}

	public static class UserProfileResponseDtoBuilder {

		private Integer status = HttpStatus.OK.value();
		private Boolean success = true;
		private String message = "회원 프로필 조회 완료";
		private UserProfileInfo data = UserProfileInfo.emptyBody();

		public UserProfileResponseDtoBuilder withStatus(Integer status) {
			this.status = status;
			return this;
		}

		public UserProfileResponseDtoBuilder withSuccess(Boolean success) {
			this.success = success;
			return this;
		}

		public UserProfileResponseDtoBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public UserProfileResponseDtoBuilder withData(UserProfileInfo data) {
			this.data = data;
			return this;
		}

		public UserProfileResponseDto build() {
			UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
			userProfileResponseDto.setStatus(status);
			userProfileResponseDto.setSuccess(success);
			userProfileResponseDto.setMessage(message);
			userProfileResponseDto.setData(data);
			return userProfileResponseDto;
		}
	}

	public static UserProfileResponseDto build(UserProfileInfo userProfileInfo) {
		return UserProfileResponseDto.getBuilder().withData(userProfileInfo).build();
	}
}
