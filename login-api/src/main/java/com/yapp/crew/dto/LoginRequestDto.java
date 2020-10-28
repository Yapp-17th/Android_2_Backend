package com.yapp.crew.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequestDto {

  @NotNull
  private String userId;
  private String userName;
  private String nickName;
  private String email;
  @NotNull
  private String accessToken;
}
