package com.yapp.crew.controller;

import com.yapp.crew.domain.errors.AlreadyAppliedException;
import com.yapp.crew.domain.errors.AlreadyApprovedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.GuestApplyNotFoundException;
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

	@ExceptionHandler(value = AlreadyApprovedException.class)
	public ResponseEntity<?> handleAlreadyApprovedException(AlreadyApprovedException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.ALREADY_APPROVED, ResponseType.ALREADY_APPROVED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = AlreadyAppliedException.class)
	public ResponseEntity<?> handleAlreadyAppliedException(AlreadyAppliedException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.ALREADY_APPLIED, ResponseType.ALREADY_APPLIED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = GuestApplyNotFoundException.class)
	public ResponseEntity<?> handleGuestApplyNotFoundException(GuestApplyNotFoundException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.ALREADY_APPROVED, ResponseType.ALREADY_APPROVED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}
}
