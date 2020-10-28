package com.yapp.crew.service;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.model.LoginResponse;
import com.yapp.crew.model.LoginResponseBody;
import com.yapp.crew.model.LoginUserInfo;
import com.yapp.crew.utils.ResponseMessage;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignInService {

  private UserRepository userRepository;
  private TokenService tokenService;

  @Autowired
  public SignInService(UserRepository userRepository, TokenService tokenService) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
  }

  public LoginResponse signIn(LoginUserInfo loginUserInfo) {
    try {
      Optional<User> existingUser = getUserByOauthId(loginUserInfo.getOauthId());
      if (existingUser.isPresent()) {
        HttpHeaders httpHeaders = tokenService.setToken(existingUser.get());
        LoginResponseBody loginResponseBody = LoginResponseBody.pass(ResponseMessage.SIGNIN_SUCCESS.getMessage());
        return new LoginResponse(httpHeaders, loginResponseBody);
      }

      return new LoginResponse(LoginResponseBody.fail(ResponseMessage.SIGNIN_FAIL_NEEDS_SIGN_UP.getMessage()));
    } catch (Exception e) {
      return new LoginResponse(LoginResponseBody.fail(ResponseMessage.SIGNIN_FAIL.getMessage()));
    }
  }

  private Optional<User> getUserByOauthId(String oauthId) {
    log.info("user 가져오기 성공");
    Optional<User> user = userRepository.findByOauthId(oauthId);
    return user;
  }
}
