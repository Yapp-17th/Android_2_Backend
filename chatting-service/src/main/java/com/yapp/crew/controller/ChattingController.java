package com.yapp.crew.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.network.HttpResponseBody;
import com.yapp.crew.payload.ApplyRequestPayload;
import com.yapp.crew.payload.ApproveRequestPayload;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class ChattingController {

	private final Auth auth;

  private final ChattingProducer chattingProducer;

  private final ChattingProducerService chattingProducerService;

  @Autowired
	public ChattingController(
					Auth auth,
					ChattingProducer chattingProducer,
					ChattingProducerService chattingProducerService
	) {
  	this.auth = auth;
  	this.chattingProducer = chattingProducer;
  	this.chattingProducerService = chattingProducerService;
	}

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

  @GetMapping(path = "/v1/chat/room/{chatRoomId}/message")
  public ResponseEntity<?> receiveChatMessages(
					@RequestHeader(value = "Authorization") String token,
  				@PathVariable("chatRoomId") Long chatRoomId
	) {
		auth.verifyToken(token);
		Long userId = auth.parseUserIdFromToken(token);

    HttpResponseBody<List<MessageResponsePayload>> responseBody = chattingProducerService.receiveChatMessages(chatRoomId, userId);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

	@GetMapping(path = "/v1/chat/room")
	public ResponseEntity<?> receiveChatRooms(@RequestHeader(value = "Authorization") String token) {
		auth.verifyToken(token);
		Long userId = auth.parseUserIdFromToken(token);

		HttpResponseBody<List<ChatRoomResponsePayload>> responseBody = chattingProducerService.receiveChatRooms(userId);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

  @PostMapping(path = "/v1/chat/room")
  public ResponseEntity<?> createChatRoom(
  				@RequestHeader(value = "Authorization") String token,
  				@Valid @RequestBody ChatRoomRequestPayload chatRoomRequestPayload
	) throws JsonProcessingException {

  	auth.verifyToken(token);
		Long guestId = auth.parseUserIdFromToken(token);

		chatRoomRequestPayload.setGuestId(guestId);

    HttpResponseBody<ChatRoomResponsePayload> responseBody = chattingProducerService.createChatRoom(chatRoomRequestPayload);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @PostMapping(path = "/v1/board/{boardId}/apply")
	public ResponseEntity<?> applyUser(
					@RequestHeader(value = "Authorization") String token,
					@Valid @RequestBody ApplyRequestPayload applyRequestPayload,
					@PathVariable("boardId") Long boardId
	) throws JsonProcessingException {

  	auth.verifyToken(token);
  	Long applierId = auth.parseUserIdFromToken(token);

		applyRequestPayload.setBoardId(boardId);
		applyRequestPayload.setApplierId(applierId);
		chattingProducerService.applyUser(applyRequestPayload);

		return ResponseEntity.status(HttpStatus.CREATED).body(applyRequestPayload);
	}

	@PostMapping(path = "/v1/board/{boardId}/approve")
	public ResponseEntity<?> approveUser(
					@RequestHeader(value = "Authorization") String token,
					@Valid @RequestBody ApproveRequestPayload approveRequestPayload,
					@PathVariable("boardId") Long boardId
	) throws JsonProcessingException {

  	auth.verifyToken(token);
  	Long hostId = auth.parseUserIdFromToken(token);

  	approveRequestPayload.setBoardId(boardId);
  	approveRequestPayload.setHostId(hostId);

  	HttpResponseBody<?> responseBody = chattingProducerService.approveUser(approveRequestPayload);
  	return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}
}
