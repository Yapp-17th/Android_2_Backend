package com.yapp.crew.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.payload.ChatRoomPayload;
import com.yapp.crew.payload.MessagePayload;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

  @GetMapping(path = "/v1/chat/message/{chatRoomId}")
  public ResponseEntity<List<Message>> receiveChatMessages(@PathVariable("chatRoomId") Long chatRoomId) {
    log.info("Receive chat messages");
    List<Message> responseBody = chattingProducerService.receiveChatMessages(chatRoomId);
    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }

	@GetMapping(path = "/v1/chat/room")
	public ResponseEntity<List<ChatRoom>> receiveChatRooms() {
		log.info("Receive chat rooms");
		List<ChatRoom> responseBody = chattingProducerService.receiveChatRooms();
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}

  @PostMapping(path = "/v1/chat/room")
  public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomPayload chatRoomPayload) throws JsonProcessingException {
  	log.info("Create chat room");
    chattingProducerService.createChatRoom(chatRoomPayload);
    return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomPayload);
  }
}