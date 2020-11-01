package com.yapp.crew.service;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.UserStatus;
import com.yapp.crew.exception.InactiveUserException;
import com.yapp.crew.exception.InternalServerErrorException;
import com.yapp.crew.exception.SuspendedUserException;
import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.model.UserAuthResponseBody;
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

  public UserAuthResponse signIn(LoginUserInfo loginUserInfo) {
    User existingUser = getUserByOauthId(loginUserInfo.getOauthId())
        .orElseThrow(InactiveUserException::new);

    if (existingUser.getStatus() == UserStatus.SUSPENDED) {
      log.info("suspended user가 로그인을 시도했습니다.");
      throw new SuspendedUserException();
    } else if (existingUser.getStatus() == UserStatus.INACTIVE) {
      log.info("inactive user가 로그인을 시도했습니다.");
      throw new InactiveUserException();
    }

    try {
      HttpHeaders httpHeaders = tokenService.setToken(existingUser);

      UserAuthResponseBody userAuthResponseBody = UserAuthResponseBody.pass(ResponseMessage.SIGNIN_SUCCESS.getMessage());
      return new UserAuthResponse(httpHeaders, userAuthResponseBody);
    } catch (Exception e) {
      log.info("Internal server error: " + e.getMessage());
      throw new InternalServerErrorException(e.getCause());
    }
  }

  private Optional<User> getUserByOauthId(String oauthId) {
    log.info("user 가져오기 성공");
    return userRepository.findByOauthId(oauthId);
  }
}
