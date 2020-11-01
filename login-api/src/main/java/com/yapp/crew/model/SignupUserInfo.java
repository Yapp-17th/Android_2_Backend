package com.yapp.crew.model;

import com.yapp.crew.dto.SignUpRequestDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupUserInfo {

  private String oauthId;
  private String accessToken;
  private String userName;
  private String nickName;
  private String email;
  private List<Long> category;
  private Long address;
  private String intro;

  public static SignupUserInfo build(SignUpRequestDto signUpRequestDto) {
    SignupUserInfo signupUserInfo = new SignupUserInfo();
    signupUserInfo.oauthId = signUpRequestDto.getUserId();
    signupUserInfo.accessToken = signUpRequestDto.getAccessToken();
    signupUserInfo.address = signUpRequestDto.getAddress();
    signupUserInfo.category = signUpRequestDto.getCategory();
    signupUserInfo.userName = signUpRequestDto.getUserName();
    signupUserInfo.nickName = signUpRequestDto.getNickName();
    signupUserInfo.email = signUpRequestDto.getEmail();
    signupUserInfo.intro = signUpRequestDto.getIntro();
    return signupUserInfo;
  }
}
