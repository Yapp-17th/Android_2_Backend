package com.yapp.crew.service;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private JwtUtils jwtUtil;

  @Autowired
  public TokenService(JwtUtils jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  public HttpHeaders setToken(User user) throws Exception {
    String accessToken = jwtUtil.createToken(user);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(jwtUtil.getHeader(), accessToken);

    return responseHeaders;
  }
}