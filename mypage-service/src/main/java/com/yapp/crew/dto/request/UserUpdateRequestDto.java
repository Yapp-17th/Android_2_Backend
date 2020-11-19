package com.yapp.crew.dto.request;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

	@NotBlank
	@Pattern(regexp = "^[가-힣a-zA-Z]*$") //한글, 영어, 띄어 쓰기 없음
	private String userName;

	@NotBlank
	@Pattern(regexp = "^[가-힣a-zA-Z1-9]*$") //한글, 영어, 띄어 쓰기 없음
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
