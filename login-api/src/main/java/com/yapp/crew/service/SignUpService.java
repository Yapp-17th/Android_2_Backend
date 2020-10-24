package com.yapp.crew.service;

import com.yapp.crew.utils.ResponseDomain;
import com.yapp.crew.utils.ResponseMessage;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignUpService {

  private UserRepository userRepository;

  @Autowired
  public SignUpService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public LoginResponseDto signUp(User user) {
    try {
      saveUser(user);
      return LoginResponseDto.pass(ResponseDomain.SIGN_UP, ResponseMessage.SIGNUP_SUCCESS.getMessage());
    } catch (Exception e) {
      return LoginResponseDto.fail(ResponseDomain.SIGN_UP, ResponseMessage.SIGNUP_FAIL.getMessage());
    }
  }

  private void saveUser(User user) {
    userRepository.save(user);
    log.info("user login 성공");
  }
}
