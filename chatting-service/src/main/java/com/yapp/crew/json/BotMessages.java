package com.yapp.crew.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@PropertySource(value = "classpath:bot-messages.json", encoding = "UTF-8")
@ConfigurationProperties
public class BotMessages {

	@JsonProperty(value = "welcome_message")
	private String welcomeMessage;
}
