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

	@Value(value = "${kafka.topics.chat-message}")
	private String chatMessageTopic;

  @Bean
  public NewTopic chatMessage() {
    return TopicBuilder
            .name(chatMessageTopic)
            .partitions(1)
            .replicas(1)
            .build();
  }
}
