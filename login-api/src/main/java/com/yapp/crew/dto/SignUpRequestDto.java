package com.yapp.crew.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignUpRequestDto {

  private String userId;
  private String userName;
  private String nickName;
  private String email;
  private String accessToken;

}
