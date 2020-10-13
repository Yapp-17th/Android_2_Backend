package com.yapp.crew.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.yapp.crew.domain.repository")
public class JpaConfiguration {

}
