package com.yapp.crew.config;

import com.yapp.crew.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class JwtConfig {

  @Value(value = "${jwt.secret}")
  private String secret;
  @Value(value = "${jwt.expiration}")
  private int expiration;
  @Value(value = "${jwt.header}")
  private String header;
  @Value(value = "${jwt.prefix}")
  private String prefix;

  @Bean
  public JwtUtils jwtUtils() {
    return new JwtUtils(secret, header, expiration, prefix);
  }
}
