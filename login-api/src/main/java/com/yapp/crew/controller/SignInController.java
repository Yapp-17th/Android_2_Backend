package com.yapp.crew.controller;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.dto.LoginResponseDto;
import com.yapp.crew.dto.UserInfoDto;
import com.yapp.crew.service.SignInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@CrossOrigin
@RestController
public class SignInController {

  private RestTemplate restTemplate;
  private SignInService signInService;

  @Autowired
  public SignInController(RestTemplate restTemplate, SignInService signInService) {
    this.restTemplate = restTemplate;
    this.signInService = signInService;
  }

  @PostMapping("/v1/user/sign-in")
  public LoginResponseDto postSignIn(UserInfoDto userInfoDto) {
    UserBuilder userBuilder = User.getBuilder();
    User user = userBuilder
        .withAccessToken(userInfoDto.getAccessToken())
        .withEmail(userInfoDto.getEmail())
        .withUsername(userInfoDto.getUserName())
        .withNickname(userInfoDto.getNickName())
        .withOauthId(userInfoDto.getUserId())
        .build();

    return signInService.signIn(user);
  }
}
