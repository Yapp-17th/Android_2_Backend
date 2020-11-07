package com.yapp.crew.controller;

import com.yapp.crew.domain.errors.AlreadyAppliedException;
import com.yapp.crew.domain.errors.AlreadyApprovedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.GuestApplyNotFoundException;
import com.yapp.crew.domain.errors.TokenRequiredException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.errors.WrongGuestException;
import com.yapp.crew.domain.errors.WrongHostException;
import com.yapp.crew.domain.errors.WrongTokenPrefixException;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.network.HttpResponseBody;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChattingExceptionHandler {

	@ExceptionHandler(value = TokenRequiredException.class)
	public ResponseEntity<?> handleTokenRequiredException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				ResponseType.TOKEN_REQUIRED,
				ResponseType.TOKEN_REQUIRED.getMessage()
		);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongTokenPrefixException.class)
	public ResponseEntity<?> handleWrongTokenPrefixException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				ResponseType.WRONG_TOKEN_PREFIX,
				ResponseType.WRONG_TOKEN_PREFIX.getMessage()
		);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<?> handleExpiredJwtException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				ResponseType.EXPIRED_TOKEN,
				ResponseType.EXPIRED_TOKEN.getMessage()
		);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MalformedJwtException.class)
	public ResponseEntity<?> handleMalformedJwtException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				ResponseType.INVALID_TOKEN,
				ResponseType.INVALID_TOKEN.getMessage()
		);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MissingRequestHeaderException.class)
	public ResponseEntity<?> handleMissingRequestHeaderException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				ResponseType.AUTHORIZATION_HEADER_REQUIRED,
				ResponseType.AUTHORIZATION_HEADER_REQUIRED.getMessage()
		);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = SignatureException.class)
	public ResponseEntity<?> handleSignatureException() {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				ResponseType.INVALID_JWT_SIGNATURE,
				ResponseType.INVALID_JWT_SIGNATURE.getMessage()
		);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException() {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.INVALID_REQUEST_BODY,
						ResponseType.INVALID_REQUEST_BODY.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = ChatRoomNotFoundException.class)
	public ResponseEntity<?> handleChatRoomNotFoundException() {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.CHATROOM_NOT_FOUND,
						ResponseType.CHATROOM_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException() {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.USER_NOT_FOUND,
						ResponseType.USER_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = BoardNotFoundException.class)
	public ResponseEntity<?> handleBoardNotFoundException() {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.BOARD_NOT_FOUND,
						ResponseType.BOARD_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongHostException.class)
	public ResponseEntity<?> handleWrongHostException() {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.WRONG_CHATROOM_HOST,
						ResponseType.WRONG_CHATROOM_HOST.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongGuestException.class)
	public ResponseEntity<?> handleWrongGuestException() {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.WRONG_CHATROOM_GUEST,
						ResponseType.WRONG_CHATROOM_GUEST.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = AlreadyApprovedException.class)
	public ResponseEntity<?> handleAlreadyApprovedException() {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.ALREADY_APPROVED,
						ResponseType.ALREADY_APPROVED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = AlreadyAppliedException.class)
	public ResponseEntity<?> handleAlreadyAppliedException(AlreadyAppliedException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.ALREADY_APPLIED,
						ResponseType.ALREADY_APPLIED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = GuestApplyNotFoundException.class)
	public ResponseEntity<?> handleGuestApplyNotFoundException(GuestApplyNotFoundException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody
				.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.ALREADY_APPROVED,
						ResponseType.ALREADY_APPROVED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}
}
