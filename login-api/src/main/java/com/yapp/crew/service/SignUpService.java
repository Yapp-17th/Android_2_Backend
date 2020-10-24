package com.yapp.crew.service;

import com.yapp.crew.domain.model.Address;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.domain.type.CityType;
import com.yapp.crew.domain.type.ExerciseType;
import com.yapp.crew.dto.SignUpRequestDto;
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

  public LoginResponseDto signUp(SignUpRequestDto signUpRequestDto) {
    try {
      UserBuilder userBuilder = User.getBuilder();
      User user = userBuilder
          .withOauthId(signUpRequestDto.getUserId())
          .withAccessToken(signUpRequestDto.getAccessToken())
          .withEmail(signUpRequestDto.getEmail())
          .withNickname(signUpRequestDto.getNickName())
          .withUsername(signUpRequestDto.getUserName())
          .build();
      saveUser(user);
      return LoginResponseDto.pass(ResponseMessage.SIGNUP_SUCCESS.getMessage());
    } catch (Exception e) {
      log.info(e.getMessage());
      return LoginResponseDto.fail(ResponseMessage.SIGNUP_FAIL.getMessage());
    }
  }

  private void saveUser(User user) {
    userRepository.save(user);
    log.info("user login 성공");
  }
}
