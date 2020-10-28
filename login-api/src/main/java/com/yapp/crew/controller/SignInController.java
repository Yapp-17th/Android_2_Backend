package com.yapp.crew.controller;

import com.yapp.crew.dto.LoginRequestDto;
import com.yapp.crew.dto.LoginResponseDto;
import com.yapp.crew.model.LoginResponse;
import com.yapp.crew.model.LoginResponseBody;
import com.yapp.crew.model.LoginUserInfo;
import com.yapp.crew.service.SignInService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    LoginResponse loginResponse = null;

    try {
      LoginUserInfo loginUserInfo = new LoginUserInfo(loginRequestDto.getUserId());
      loginResponse = signInService.signIn(loginUserInfo);

      HttpHeaders httpHeaders = loginResponse.getHttpHeaders();
      LoginResponseDto loginResponseDto = LoginResponseDto.build(loginResponse.getLoginResponseBody());
      if (httpHeaders != null) {
        return ResponseEntity.ok().headers(httpHeaders).body(loginResponseDto);
      }

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponseDto.build(LoginResponseBody.fail(e.getMessage())));
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponseDto.build(loginResponse.getLoginResponseBody()));
  }
}
