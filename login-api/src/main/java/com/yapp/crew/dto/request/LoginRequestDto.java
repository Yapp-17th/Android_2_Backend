package com.yapp.crew.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequestDto {

  @NotBlank
  private String userId;
  private String userName;
  private String nickName;
  private String email;
  @NotBlank
  private String accessToken;
}
