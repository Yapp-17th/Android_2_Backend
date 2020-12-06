package com.yapp.crew.websocket;

import com.yapp.crew.auth.Auth;
import com.yapp.crew.domain.repository.UserRepository;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j(topic = "Custom Handshake Interceptor")
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

  @Value(value = "${jwt.header}")
  private String AuthorizationHeader;

  private final Auth auth;
  private final UserRepository userRepository;

  public CustomHandshakeInterceptor(Auth auth, UserRepository userRepository) {
    this.auth = auth;
    this.userRepository = userRepository;
  }

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      String token = servletRequest.getServletRequest().getHeader(AuthorizationHeader);
      log.info("Given token -> {}", token);
      // 잘 불러와진다면 여기서 사용자 인증 추가
    }
    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

  }
}
