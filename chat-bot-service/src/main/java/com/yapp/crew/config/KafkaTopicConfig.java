package com.yapp.crew.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile(value = "local")
public class KafkaTopicConfig {

	@Value(value = "${kafka.topics.welcome-message}")
	private String welcomeMessageTopic;

	@Value(value = "${kafka.topics.apply-user}")
	private String applyUserTopic;

	@Value(value = "${kafka.topics.approve-user}")
	private String approveUserTopic;

	@Bean
	public NewTopic welcomeMessage() {
		return TopicBuilder
						.name(welcomeMessageTopic)
						.partitions(1)
						.replicas(1)
						.build();
	}

	@Bean
	public NewTopic applyUser() {
		return TopicBuilder
						.name(applyUserTopic)
						.partitions(1)
						.replicas(1)
						.build();
	}

	@Bean
	public NewTopic approveUser() {
		return TopicBuilder
						.name(approveUserTopic)
						.partitions(1)
						.replicas(1)
						.build();
	}
}
