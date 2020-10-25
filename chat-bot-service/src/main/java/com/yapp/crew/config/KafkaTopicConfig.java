package com.yapp.crew.config;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile(value = "dev")
public class KafkaTopicConfig {

	@Value(value = "${kafka.topics.welcome-message}")
	private String welcomeMessageTopic;

	@Value(value = "${kafka.topics.request-user-profile}")
	private String requestUserProfileTopic;

	@Value(value = "${kafka.topics.accept-user}")
	private String acceptUserTopic;

	@Bean
	public NewTopic welcomeMessage() {
		return TopicBuilder
						.name(welcomeMessageTopic)
						.partitions(1)
						.replicas(1)
						.build();
	}

	@Bean
	public NewTopic requestUserProfile() {
		return TopicBuilder
						.name(requestUserProfileTopic)
						.partitions(1)
						.replicas(1)
						.build();
	}

	@Bean
	public NewTopic acceptUser() {
		return TopicBuilder
						.name(acceptUserTopic)
						.partitions(1)
						.replicas(1)
						.build();
	}
}
