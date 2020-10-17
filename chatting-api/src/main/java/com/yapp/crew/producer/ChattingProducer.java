package com.yapp.crew.producer;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.crew.payload.MessagePayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class ChattingProducer {

  @Autowired
  private KafkaTemplate<Long, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  public ListenableFuture<SendResult<Long, String>> sendMessage(MessagePayload messagePayload) throws JsonProcessingException {
    Long key = messagePayload.getChatRoomId();
    String value = objectMapper.writeValueAsString(messagePayload);

    ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, "explanet-dev");
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

  public SendResult<Long, String> sendMessageSynchronously(MessagePayload messagePayload) throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
    Long key = messagePayload.getChatRoomId();
    String value = objectMapper.writeValueAsString(messagePayload);
    SendResult<Long, String> sendResult = null;

    try {
      ProducerRecord<Long, String> producerRecord = buildProducerRecord(key, value, "explanet-dev");
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
