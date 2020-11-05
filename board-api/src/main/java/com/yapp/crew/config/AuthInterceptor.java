package com.yapp.crew.config;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.domain.errors.InactiveUserException;
import com.yapp.crew.domain.errors.SuspendedUserException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.UserStatus;
import com.yapp.crew.domain.type.ResponseType;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
@Component
@NoArgsConstructor
public class AuthInterceptor extends HandlerInterceptorAdapter {

  private UserRepository userRepository;
  private Auth auth;

  @Autowired
  public AuthInterceptor(UserRepository userRepository, Auth auth) {
    this.userRepository = userRepository;
    this.auth = auth;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    verifyToken(token);

    Long userId = auth.parseUserIdFromToken(token);
    log.info("[Request User ID] " + userId);
    User user = findUserById(userId)
        .orElseThrow(() -> new UserNotFoundException("[INTERCEPTOR Exception] user not found"));

    checkUserStatus(user.getStatus());
    return super.preHandle(request, response, handler);
  }

  private void checkUserStatus(UserStatus userStatus) {
    if (userStatus == UserStatus.INACTIVE) {
      throw new InactiveUserException("inactive user");
    } else if (userStatus == UserStatus.SUSPENDED) {
      throw new SuspendedUserException("suspended user");
    }
  }

  private Optional<User> findUserById(Long userId) {
    return userRepository.findUserById(userId);
  }

  private void verifyToken(String token) {
    auth.verifyToken(token);
  }
}
