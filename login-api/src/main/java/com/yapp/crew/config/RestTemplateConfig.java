package com.yapp.crew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  private static final int CONNECT_TIMEOUT = 3000;
  private static final int REQUEST_TIMEOUT = 30000;
  private static final int READ_TIMEOUT = 30000;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate(clientHttpRequestFactory());
  }

  @Bean
  public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
    HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    httpRequestFactory.setConnectionRequestTimeout(REQUEST_TIMEOUT);
    httpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
    httpRequestFactory.setReadTimeout(READ_TIMEOUT);
    return httpRequestFactory;
  }
}
