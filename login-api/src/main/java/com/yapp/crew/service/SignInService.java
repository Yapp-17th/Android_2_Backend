package com.yapp.crew.service;

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
  private SignUpService signUpService;

  @Autowired
  public SignInService(UserRepository userRepository, SignUpService signUpService) {
    this.userRepository = userRepository;
    this.signUpService = signUpService;
  }

  public LoginResponseDto signIn(User user) {
    try {
      Optional<User> existingUser = getUserByOauthId(user.getOauthId());
      if (existingUser.isPresent()) {
        return LoginResponseDto.pass(ResponseDomain.SIGN_IN, ResponseMessage.SIGNIN_SUCCESS.getMessage());
      } else {
        return signUpService.signUp(user);
      }
    } catch (Exception e) {
      return LoginResponseDto.fail(ResponseDomain.SIGN_IN, ResponseMessage.SIGNIN_FAIL.getMessage());
    }
  }

  private Optional<User> getUserByOauthId(String oauthId) {
    log.info("user 가져오기 성공");
    Optional<User> user = userRepository.findUserByOauthId(oauthId);
    return user;
  }
}
