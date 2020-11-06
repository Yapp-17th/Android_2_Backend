package com.yapp.crew.controller;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.network.HttpResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatBotExceptionHandler {

	@ExceptionHandler(value = ChatRoomNotFoundException.class)
	public ResponseEntity<?> handleChatRoomNotFoundException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.CHATROOM_NOT_FOUND, ResponseType.CHATROOM_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.USER_NOT_FOUND, ResponseType.USER_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = BoardNotFoundException.class)
	public ResponseEntity<?> handleBoardNotFoundException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.BOARD_NOT_FOUND, ResponseType.BOARD_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}
}
