package com.yapp.crew.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.network.HttpResponseBody;
import com.yapp.crew.payload.ApplyRequestPayload;
import com.yapp.crew.payload.ApproveRequestPayload;
import com.yapp.crew.payload.ChatRoomRequestPayload;
import com.yapp.crew.payload.ChatRoomResponsePayload;
import com.yapp.crew.payload.MessageRequestPayload;
import com.yapp.crew.payload.MessageResponsePayload;
import com.yapp.crew.producer.ChattingProducer;
import com.yapp.crew.service.ChattingService;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Chatting Controller")
@CrossOrigin
@RestController
public class ChattingController {

	private final ChattingProducer chattingProducer;
	private final ChattingService chattingService;

	@Autowired
	public ChattingController(
			ChattingProducer chattingProducer,
			ChattingService chattingService
	) {
		this.chattingProducer = chattingProducer;
		this.chattingService = chattingService;
	}

	@MessageMapping(value = "/v1/chat/message")
	public void sendMessageByWebsocket(@Payload @Valid MessageRequestPayload messageRequestPayload) throws JsonProcessingException {
		chattingProducer.sendMessage(messageRequestPayload);
	}

	@MessageMapping(value = "/v1/chat/message/update")
	public void updateMessageByWebsocket(@Payload @Valid MessageRequestPayload messageRequestPayload) {
		chattingService.messageBulkUpdate(messageRequestPayload);
	}

	@PostMapping(path = "/v1/chat/message")
	public ResponseEntity<?> sendMessageByHttpRequest(@RequestBody MessageRequestPayload messageRequestPayload) throws JsonProcessingException {
		chattingProducer.sendMessage(messageRequestPayload);
		return ResponseEntity.status(HttpStatus.CREATED).body(messageRequestPayload);
	}

	@PostMapping(path = "/v2/chat/message")
	public ResponseEntity<?> sendMessageSynchronously(@RequestBody MessageRequestPayload messageRequestPayload)
			throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {

		chattingProducer.sendMessageSynchronously(messageRequestPayload);
		return ResponseEntity.status(HttpStatus.CREATED).body(messageRequestPayload);
	}

	@GetMapping(path = "/v1/chat/room/{chatRoomId}/message")
	public ResponseEntity<?> receiveChatMessages(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "chatRoomId") long chatRoomId
	) {
		log.info("Receive chat messages -> userId: {}, chatRoomId: {}", userId, chatRoomId);
		HttpResponseBody<List<MessageResponsePayload>> responseBody = chattingService.receiveChatMessages(chatRoomId, userId);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@PutMapping(path = "/v1/chat/room/{chatRoomId}/message/{messageId}")
	public ResponseEntity<?> updateMessageIsRead(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "chatRoomId") long chatRoomId,
			@PathVariable(name = "messageId") long messageId
	) {
		log.info("Update message isRead -> userId: {}, chatRoomId: {}, messageId: {}", userId, chatRoomId, messageId);
		HttpResponseBody<?> responseBody = chattingService.messageUpdate(userId, chatRoomId, messageId);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@GetMapping(path = "/v1/chat/room")
	public ResponseEntity<?> receiveChatRooms(@RequestHeader(value = "userId") long userId) {
		log.info("Receive chat rooms -> userId: {}", userId);
		HttpResponseBody<List<ChatRoomResponsePayload>> responseBody = chattingService.receiveChatRooms(userId);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@PostMapping(path = "/v1/chat/room")
	public ResponseEntity<?> createChatRoom(
			@RequestHeader(value = "userId") long userId,
			@Valid @RequestBody ChatRoomRequestPayload chatRoomRequestPayload
	) throws JsonProcessingException {

		chatRoomRequestPayload.setGuestId(userId);
		log.info("Create chat room -> userId: {}, payload: {}", userId, chatRoomRequestPayload);

		HttpResponseBody<ChatRoomResponsePayload> responseBody = chattingService.createChatRoom(chatRoomRequestPayload);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@DeleteMapping(path = "/v1/chat/room/{chatRoomId}")
	public ResponseEntity<?> exitChatRoom(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "chatRoomId") long chatRoomId
	) throws JsonProcessingException {
		log.info("Exit chat room -> userId: {}, chatRoomId: {}", userId, chatRoomId);

		HttpResponseBody<?> responseBody = chattingService.exitChatRoom(userId, chatRoomId);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@PostMapping(path = "/v1/board/{boardId}/apply")
	public ResponseEntity<?> applyUser(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "boardId") long boardId,
			@Valid @RequestBody ApplyRequestPayload applyRequestPayload
	) throws JsonProcessingException {

		applyRequestPayload.setBoardId(boardId);
		applyRequestPayload.setApplierId(userId);
		log.info("Apply user -> userId: {}, payload: {}", userId, applyRequestPayload);

		HttpResponseBody<?> responseBody = chattingService.applyUser(applyRequestPayload);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}

	@PostMapping(path = "/v1/board/{boardId}/approve")
	public ResponseEntity<?> approveUser(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "boardId") long boardId,
			@Valid @RequestBody ApproveRequestPayload approveRequestPayload
	) throws JsonProcessingException {

		approveRequestPayload.setBoardId(boardId);
		approveRequestPayload.setHostId(userId);
		log.info("Approve user -> userId: {}, payload: {}", userId, approveRequestPayload);

		HttpResponseBody<?> responseBody = chattingService.approveUser(approveRequestPayload);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@DeleteMapping(path = "/v1/board/{boardId}/approve")
	public ResponseEntity<?> disapproveUser(
			@RequestHeader(value = "userId") long userId,
			@PathVariable(name = "boardId") long boardId,
			@Valid @RequestBody ApproveRequestPayload approveRequestPayload
	) throws JsonProcessingException {

		approveRequestPayload.setBoardId(boardId);
		approveRequestPayload.setHostId(userId);
		log.info("Disapprove user -> userId: {}, payload: {}", userId, approveRequestPayload);

		HttpResponseBody<?> responseBody = chattingService.disapproveUser(approveRequestPayload);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}
}
