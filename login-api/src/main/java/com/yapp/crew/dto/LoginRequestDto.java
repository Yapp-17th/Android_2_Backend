package com.yapp.crew.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequestDto {

  private String userId;
  private String userName;
  private String nickName;
  private String email;
  private String accessToken;

}
