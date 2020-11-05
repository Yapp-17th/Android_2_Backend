package com.yapp.crew.dto.request;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignUpRequestDto {

  @NotNull
  private String userId;

  @NotNull
  private String userName;

  @NotNull
  private String nickName;

  @NotNull
  @NotBlank(message = "{ACCOUNT.CREATE.EMAIL_NOT_BLANK}")
  @Email(message = "{ACCOUNT.CREATE.EMAIL_IS_NOT_VALID}")
  private String email;

  @NotNull
  private String accessToken;

  @NotNull
  private List<Long> category;

  @NotNull
  private long address;

  @NotNull
  private String intro;
}
