package com.yapp.crew.producer;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.payload.ApplyRequestPayload;
import com.yapp.crew.payload.ApproveRequestPayload;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.WelcomeMessageRequestPayload;
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
public class ChattingProducer {

	@Value(value = "${kafka.topics.chat-message}")
	private String chatMessageTopic;

	@Value(value = "${kafka.topics.guideline-message}")
	private String guidelineMessageTopic;

	@Value(value = "${kafka.topics.apply-user}")
	private String applyUserTopic;

	@Value(value = "${kafka.topics.approve-user}")
	private String approveUserTopic;

  private final KafkaTemplate<Long, String> kafkaTemplate;

  private final ObjectMapper objectMapper;

  @Autowired
	public ChattingProducer(
					KafkaTemplate<Long, String> kafkaTemplate,
					ObjectMapper objectMapper
	) {
  	this.kafkaTemplate = kafkaTemplate;
  	this.objectMapper = objectMapper;
	}

  public ListenableFuture<SendResult<Long, String>> sendMessage(MessageRequestPayload messageRequestPayload) throws JsonProcessingException {
    Long key = messageRequestPayload.getChatRoomId();
    String value = objectMapper.writeValueAsString(messageRequestPayload);

    ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, chatMessageTopic);
    ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate.send(producerRecord);
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

  public ListenableFuture<SendResult<Long, String>> sendGuidelineBotMessage(WelcomeMessageRequestPayload welcomeMessageRequestPayload) throws JsonProcessingException {
		Long key = welcomeMessageRequestPayload.getChatRoomId();
		String value = objectMapper.writeValueAsString(welcomeMessageRequestPayload);

		ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, guidelineMessageTopic);
		ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate.send(producerRecord);
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

	public ListenableFuture<SendResult<Long, String>> applyUser(ApplyRequestPayload applyRequestPayload) throws JsonProcessingException {
		Long key = applyRequestPayload.getBoardId();
		String value = objectMapper.writeValueAsString(applyRequestPayload);

		ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, applyUserTopic);
		ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate.send(producerRecord);
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

	public ListenableFuture<SendResult<Long, String>> approveUser(ApproveRequestPayload approveRequestPayload) throws JsonProcessingException {
		Long key = approveRequestPayload.getBoardId();
		String value = objectMapper.writeValueAsString(approveRequestPayload);

		ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, approveUserTopic);
		ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate.send(producerRecord);
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

  public SendResult<Long, String> sendMessageSynchronously(MessageRequestPayload messageRequestPayload) throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
    Long key = messageRequestPayload.getChatRoomId();
    String value = objectMapper.writeValueAsString(messageRequestPayload);
    SendResult<Long, String> sendResult = null;

    try {
      ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, chatMessageTopic);
      sendResult = kafkaTemplate.send(producerRecord).get(1, TimeUnit.SECONDS);
    } catch (ExecutionException | InterruptedException ex) {
      log.error("ExecutionException / InterruptedException sending the message and exception is {}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("Exception sending the message and exception is {}", ex.getMessage());
      throw ex;
    }
    return sendResult;
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
    log.info("Message send successfully for the key: {} and the value is {}, partition is {}", key, value, result.getRecordMetadata().partition());
  }
}
