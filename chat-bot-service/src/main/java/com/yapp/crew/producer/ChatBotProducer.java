package com.yapp.crew.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.payload.MessageRequestPayload;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class ChatBotProducer {

	@Value(value = "${kafka.topics.chat-message}")
	private String chatMessageTopic;

	private final KafkaTemplate<Long, String> kafkaTemplate;

	private final ObjectMapper objectMapper;

	@Autowired
	public ChatBotProducer(
			KafkaTemplate<Long, String> kafkaTemplate,
			ObjectMapper objectMapper
	) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	public ListenableFuture<SendResult<Long, String>> sendBotMessage(
			MessageRequestPayload messageRequestPayload) throws JsonProcessingException {
		Long key = messageRequestPayload.getChatRoomId();
		String value = objectMapper.writeValueAsString(messageRequestPayload);

		ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, chatMessageTopic);
		ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate
				.send(producerRecord);
		listenableFuture.addCallback(new ListenableFutureCallback<>() {
			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);
			}

			@Override
			public void onSuccess(SendResult<Long, String> result) {
				handleSuccess(key, value, result);
			}
		});
		return listenableFuture;
	}

	private ProducerRecord<Long, String> buildProducerRecord(Long key, String value, String topic) {
		List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
		return new ProducerRecord<Long, String>(topic, null, key, value, recordHeaders);
	}

	private void handleFailure(Long key, String value, Throwable ex) {
		log.error("Error sending the message and exception is {}", ex.getMessage());
		try {
			throw ex;
		} catch (Throwable throwable) {
			log.error("Error in onFailure(): {}", throwable.getMessage());
		}
	}

	private void handleSuccess(Long key, String value, SendResult<Long, String> result) {
		log.info("Message send successfully for the key: {} and the value is {}, partition is {}", key,
				value, result.getRecordMetadata().partition());
	}
}
