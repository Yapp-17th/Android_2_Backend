package com.yapp.crew.controller;

import com.yapp.crew.dto.LoginResponseDto;
import com.yapp.crew.dto.SignUpRequestDto;
import com.yapp.crew.model.LoginResponse;
import com.yapp.crew.model.LoginResponseBody;
import com.yapp.crew.model.SignupUserInfo;
import com.yapp.crew.service.SignUpService;
import com.yapp.crew.utils.ResponseMessage;
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
public class SignUpController {

  private SignUpService signUpService;

  @Autowired
  public SignUpController(SignUpService signUpService) {
    this.signUpService = signUpService;
  }

  @PostMapping(path = "/v1/user/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postSignIn(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
    LoginResponse loginResponse = null;

    try {
      SignupUserInfo signupUserInfo = SignupUserInfo.build(signUpRequestDto);
      loginResponse = signUpService.signUp(signupUserInfo);

      LoginResponseDto loginResponseDto = LoginResponseDto.build(loginResponse.getLoginResponseBody());
      HttpHeaders httpHeaders = loginResponse.getHttpHeaders();

      if (httpHeaders != null) {
        return ResponseEntity.ok().headers(httpHeaders).body(loginResponseDto);
      }

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponseDto.build(LoginResponseBody.fail(e.getMessage())));
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponseDto.build(LoginResponseBody.fail(ResponseMessage.TOKEN_NOT_CREATED.getMessage())));
  }
}
