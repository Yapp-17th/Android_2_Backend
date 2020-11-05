package com.yapp.crew.controller;

import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.dto.request.LoginRequestDto;
import com.yapp.crew.dto.response.UserAuthResponseDto;
import com.yapp.crew.model.LoginUserInfo;
import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.service.SignInService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    LoginUserInfo loginUserInfo = new LoginUserInfo(loginRequestDto.getUserId());
    UserAuthResponse userAuthResponse = signInService.signIn(loginUserInfo);

    HttpHeaders httpHeaders = userAuthResponse.getHttpHeaders();
    UserAuthResponseDto userAuthResponseDto = UserAuthResponseDto.build(userAuthResponse.getUserAuthResponseBody());
    if (httpHeaders != null) {
      return ResponseEntity.ok().headers(httpHeaders).body(userAuthResponseDto);
    }

    throw new InternalServerErrorException("internal server error");
  }
}
