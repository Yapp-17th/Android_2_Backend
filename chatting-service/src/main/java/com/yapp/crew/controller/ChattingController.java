package com.yapp.crew.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.network.HttpResponseBody;
import com.yapp.crew.payload.ChatRoomRequestPayload;
import com.yapp.crew.payload.ChatRoomResponsePayload;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.MessageResponsePayload;
import com.yapp.crew.producer.ChattingProducer;
import com.yapp.crew.service.ChattingProducerService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class ChattingController {

  @Autowired
  private ChattingProducer chattingProducer;

  @Autowired
  private ChattingProducerService chattingProducerService;

  @MessageMapping(value = "/v1/chat/message")
  public void sendMessageByWebsocket(MessageRequestPayload messageRequestPayload) throws JsonProcessingException {
    log.info("Before-sendMessageByWebsocket");

    chattingProducer.sendMessage(messageRequestPayload);

    log.info("After-sendMessageByWebsocket");
  }

  @PostMapping(path = "/v1/chat/message")
  public ResponseEntity<?> sendMessageByHttpRequest(@RequestBody MessageRequestPayload messageRequestPayload) throws JsonProcessingException {
    log.info("Before-sendMessageByHttpRequest");

    chattingProducer.sendMessage(messageRequestPayload);

    log.info("After-sendMessageByHttpRequest");

    return ResponseEntity.status(HttpStatus.CREATED).body(messageRequestPayload);
  }

  @PostMapping(path = "/v2/chat/message")
  public ResponseEntity<?> sendMessageSynchronously(@RequestBody MessageRequestPayload messageRequestPayload)
          throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {

    log.info("Before-sendMessageSynchronously");

    chattingProducer.sendMessageSynchronously(messageRequestPayload);

    log.info("After-sendMessageSynchronously");

    return ResponseEntity.status(HttpStatus.CREATED).body(messageRequestPayload);
  }

  @GetMapping(path = "/v1/chat/message/{chatRoomId}")
  public ResponseEntity<?> receiveChatMessages(@PathVariable("chatRoomId") Long chatRoomId) {
    log.info("Receive chat messages");
    HttpResponseBody<List<MessageResponsePayload>> responseBody = chattingProducerService.receiveChatMessages(chatRoomId);
    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }

	@GetMapping(path = "/v1/chat/room")
	public ResponseEntity<List<ChatRoomResponsePayload>> receiveChatRooms() {
		log.info("Receive chat rooms");
		List<ChatRoomResponsePayload> responseBody = chattingProducerService.receiveChatRooms();
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}

  @PostMapping(path = "/v1/chat/room")
  public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomRequestPayload chatRoomRequestPayload) throws JsonProcessingException {
  	log.info("Create chat room");
    chattingProducerService.createChatRoom(chatRoomRequestPayload);
    return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomRequestPayload);
  }
}
