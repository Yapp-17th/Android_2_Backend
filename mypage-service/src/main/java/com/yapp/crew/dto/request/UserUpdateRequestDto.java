package com.yapp.crew.dto.request;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

	@NotBlank
	private String userName;

	@NotBlank
	private String nickName;

	@NotBlank(message = "{ACCOUNT.CREATE.EMAIL_NOT_BLANK}")
	@Email(message = "{ACCOUNT.CREATE.EMAIL_IS_NOT_VALID}")
	private String email;

	@NotNull
	private List<Long> category;

	@NotNull
	private Long address;

	@NotBlank
	private String intro;
}
