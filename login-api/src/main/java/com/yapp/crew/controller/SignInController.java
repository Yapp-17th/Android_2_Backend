package com.yapp.crew.controller;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.dto.LoginResponseDto;
import com.yapp.crew.dto.LoginRequestDto;
import com.yapp.crew.model.LoginUserInfo;
import com.yapp.crew.service.SignInService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class SignInController {

  private SignInService signInService;

  @Autowired
  public SignInController(SignInService signInService) {
    this.signInService = signInService;
  }

  @PostMapping(path = "/v1/user/sign-in", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postSignIn(@RequestBody @Valid LoginRequestDto loginRequestDto) {
    LoginResponseDto responseBody = null;

    try {
      LoginUserInfo loginUserInfo = new LoginUserInfo(loginRequestDto.getUserId());
      responseBody = signInService.signIn(loginUserInfo);

      if (responseBody.getStatus() == HttpStatus.OK.value()) {
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
      }

    } catch (Exception e) {
      responseBody = LoginResponseDto.fail(e.getMessage());
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
  }
}
