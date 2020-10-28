package com.yapp.crew.controller;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.dto.LoginResponseDto;
import com.yapp.crew.dto.LoginRequestDto;
import com.yapp.crew.dto.SignUpRequestDto;
import com.yapp.crew.model.SignupUserInfo;
import com.yapp.crew.service.SignUpService;
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
public class SignUpController {

  private SignUpService signUpService;

  @Autowired
  public SignUpController(SignUpService signUpService) {
    this.signUpService = signUpService;
  }

  @PostMapping(path = "/v1/user/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postSignIn(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
    LoginResponseDto responseBody = null;

    try {
      SignupUserInfo signupUserInfo = SignupUserInfo.build(signUpRequestDto);
      responseBody = signUpService.signUp(signupUserInfo);

      return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    } catch (Exception e) {
      responseBody = LoginResponseDto.fail(e.getMessage());
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
  }
}
