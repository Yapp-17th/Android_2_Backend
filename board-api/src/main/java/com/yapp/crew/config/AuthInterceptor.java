package com.yapp.crew.config;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.UserStatus;
import com.yapp.crew.exception.SuspendedUserException;
import com.yapp.crew.exception.WrongTokenException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@NoArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  private UserRepository userRepository;
  private Auth auth;
  private final String HEADER_TOKEN_KEY = "Authorization";

  @Autowired
  public AuthInterceptor(UserRepository userRepository, Auth auth) {
    this.userRepository = userRepository;
    this.auth = auth;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String token = request.getHeader(HEADER_TOKEN_KEY);
    verifyToken(token);
    Long userId = auth.parseUserIdFromToken(token);
    User user = findUserById(userId)
        .orElseThrow(SuspendedUserException::new);

    return user.getStatus() == UserStatus.ACTIVE;
  }

  private Optional<User> findUserById(Long userId) {
    log.info("user 찾기 성공");
    return userRepository.findUserById(userId);
  }

  private void verifyToken(String token) throws WrongTokenException {
    auth.verifyToken(token);
  }
}
