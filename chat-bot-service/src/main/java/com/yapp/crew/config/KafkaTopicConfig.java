package com.yapp.crew.config;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile(value = "dev")
public class KafkaTopicConfig {

	@Bean
	public NewTopic requestUserProfile() {
		return TopicBuilder
						.name("request-user-profile")
						.partitions(1)
						.replicas(1)
						.build();
	}

	@Bean
	public NewTopic acceptUser() {
		return TopicBuilder
						.name("accept-user")
						.partitions(1)
						.replicas(1)
						.build();
	}
}
