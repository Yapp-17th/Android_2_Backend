package com.yapp.crew.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile(value = "prod")
public class KafkaTopicConfig {

	@Value(value = "${kafka.topics.guideline-message}")
	private String guidelineMessageTopic;

	@Value(value = "${kafka.topics.apply-user}")
	private String applyUserTopic;

	@Value(value = "${kafka.topics.approve-user}")
	private String approveUserTopic;

	@Value(value = "${kafka.topics.disapprove-user}")
	private String disapproveUserTopic;

	@Value(value = "${kafka.topics.board-finished}")
	private String boardFinishedTopic;

	@Value(value = "${kafka.topics.user-exited}")
	private String userExitedTopic;

	@Bean
	public NewTopic guidelineMessage() {
		return TopicBuilder
				.name(guidelineMessageTopic)
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

	@Bean
	public NewTopic disapproveUser() {
		return TopicBuilder
				.name(disapproveUserTopic)
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	public NewTopic boardFinished() {
		return TopicBuilder
				.name(boardFinishedTopic)
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	public NewTopic userExited() {
		return TopicBuilder
				.name(userExitedTopic)
				.partitions(1)
				.replicas(1)
				.build();
	}
}
