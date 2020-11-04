package com.yapp.crew.service;

import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.model.UserAuthResponseBody;
import com.yapp.crew.config.JwtUtils;
import com.yapp.crew.utils.ResponseMessage;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class WithdrawService {

  private UserRepository userRepository;
  private JwtUtils jwtUtils;

  @Autowired
  public WithdrawService(UserRepository userRepository, JwtUtils jwtUtils) {
    this.userRepository = userRepository;
    this.jwtUtils = jwtUtils;
  }


  public UserAuthResponse withdraw(String token) {
    try {
      Long userId = Long.valueOf(jwtUtils.getUserIdFromToken(token));
      log.info("userId: " + userId);

      User user = getUserByUserId(userId)
          .orElseThrow(() -> new UserNotFoundException("user not found"));

      updateUserInActive(user);
    } catch (Exception e) {
      throw new InternalServerErrorException("internal server error");
    }

    return new UserAuthResponse(UserAuthResponseBody.pass(ResponseMessage.WITHDRAW_SUCCESS.getMessage()));
  }

  @Transactional
  public void updateUserInActive(User user) {
    user.setUserStatusInActive();
    userRepository.save(user);
    log.info("user update 완료");
  }

  private Optional<User> getUserByUserId(Long userId) {
    log.info("user 가져오기 성공");
    return userRepository.findUserById(userId);
  }
}
