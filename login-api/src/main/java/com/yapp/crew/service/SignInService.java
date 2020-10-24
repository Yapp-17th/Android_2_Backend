package com.yapp.crew.service;

import com.yapp.crew.dto.LoginRequestDto;
import com.yapp.crew.utils.ResponseDomain;
import com.yapp.crew.utils.ResponseMessage;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.dto.LoginResponseDto;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignInService {

  private UserRepository userRepository;

  @Autowired
  public SignInService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public LoginResponseDto signIn(LoginRequestDto loginRequestDto) {
    try {
      Optional<User> existingUser = getUserByOauthId(loginRequestDto.getUserId());
      if (existingUser.isPresent()) {
        return LoginResponseDto.pass(ResponseMessage.SIGNIN_SUCCESS.getMessage());
      }
      return LoginResponseDto.fail(ResponseMessage.SIGNIN_FAIL_NEEDS_SIGN_UP.getMessage());
    } catch (Exception e) {
      return LoginResponseDto.fail(ResponseMessage.SIGNIN_FAIL.getMessage());
    }
  }

  private Optional<User> getUserByOauthId(String oauthId) {
    log.info("user 가져오기 성공");
    Optional<User> user = userRepository.findByOauthId(oauthId);
    return user;
  }
}
