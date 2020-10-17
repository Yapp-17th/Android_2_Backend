package com.yapp.crew.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.payload.ChatRoomPayload;
import com.yapp.crew.payload.MessagePayload;
import com.yapp.crew.producer.ChattingProducer;
import com.yapp.crew.service.ChattingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
public class ChattingController {

  @Autowired
  private ChattingProducer chattingProducer;

  @Autowired
  private ChattingService chattingService;

  @MessageMapping(value = "/v1/chat/message")
  public void sendMessageByWebsocket(MessagePayload messagePayload) throws JsonProcessingException {
    log.info("Before-sendMessageByWebsocket");

    chattingProducer.sendMessage(messagePayload);

    log.info("After-sendMessageByWebsocket");
  }

  @PostMapping(path = "/v1/chat/message")
  public ResponseEntity<?> sendMessageByHttpRequest(@RequestBody MessagePayload messagePayload) throws JsonProcessingException {
    log.info("Before-sendMessageByHttpRequest");

    chattingProducer.sendMessage(messagePayload);

    log.info("After-sendMessageByHttpRequest");

    return ResponseEntity.status(HttpStatus.CREATED).body(messagePayload);
  }

  @PostMapping(path = "/v2/chat/message")
  public ResponseEntity<?> sendMessageSynchronously(@RequestBody MessagePayload messagePayload)
          throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {

    log.info("Before-sendMessageSynchronously");

    chattingProducer.sendMessageSynchronously(messagePayload);

    log.info("After-sendMessageSynchronously");

    return ResponseEntity.status(HttpStatus.CREATED).body(messagePayload);
  }

  @PostMapping(path = "/v1/chat/room")
  public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomPayload chatRoomPayload) throws JsonProcessingException {
    chattingService.createChatRoom(chatRoomPayload);
    return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomPayload);
  }
}
