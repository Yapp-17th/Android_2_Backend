package com.yapp.crew.controller;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.dto.LoginResponseDto;
import com.yapp.crew.dto.LoginRequestDto;
import com.yapp.crew.dto.SignUpRequestDto;
import com.yapp.crew.service.SignUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class SignUpController {
  private SignUpService signUpService;

  @Autowired
  public SignUpController(SignUpService signUpService) {
    this.signUpService = signUpService;
  }

  @PostMapping(path = "/v1/user/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
  public LoginResponseDto postSignIn(@RequestBody SignUpRequestDto signUpRequestDto) {
    UserBuilder userBuilder = User.getBuilder();
    User user = userBuilder
        .withOauthId(signUpRequestDto.getUserId())
        .build();

    return signUpService.signUp(signUpRequestDto);
  }
}
