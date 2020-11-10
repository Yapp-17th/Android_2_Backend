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

	@JsonProperty(value = "guideline_message")
	private String guidelineMessage;

	@JsonProperty(value = "apply_message")
	private String applyMessage;

	@JsonProperty(value = "approve_message")
	private String approveMessage;

	@JsonProperty(value = "disapprove_message")
	private String disapproveMessage;

	@JsonProperty(value = "profile_message")
	private String profileMessage;

	@JsonProperty(value = "board_finished")
	private String boardFinished;
}
