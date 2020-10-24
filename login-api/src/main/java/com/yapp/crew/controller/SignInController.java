package com.yapp.crew.controller;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.dto.LoginResponseDto;
import com.yapp.crew.dto.LoginRequestDto;
import com.yapp.crew.service.SignInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SignInController {
  private SignInService signInService;

  @Autowired
  public SignInController(SignInService signInService) {
    this.signInService = signInService;
  }

  @PostMapping(path = "/v1/user/sign-in", produces = MediaType.APPLICATION_JSON_VALUE)
  public LoginResponseDto postSignIn(@RequestBody LoginRequestDto loginRequestDto) {
    return signInService.signIn(loginRequestDto);
  }
}
