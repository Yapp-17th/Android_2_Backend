package com.yapp.crew.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  private final String[] INTERCEPTOR_WHITE_LIST = {
      "**/sign-up",
      "**/sign-in"
  };

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthInterceptor())
        .addPathPatterns("/*")
        .excludePathPatterns("sign-up", "sign-in");
  }
}
