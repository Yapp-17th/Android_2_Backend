package com.yapp.crew.config;

import com.yapp.crew.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private int expiration;

  @Value("${jwt.header}")
  private String header;

  @Bean
  public JwtUtils jwtUtil() {
    return new JwtUtils(secret, expiration);
  }
}
