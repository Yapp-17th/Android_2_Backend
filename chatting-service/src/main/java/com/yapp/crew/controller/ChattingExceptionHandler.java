package com.yapp.crew.controller;

import com.yapp.crew.domain.errors.AlreadyApprovedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
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
	public ResponseEntity<?> handleTokenRequiredException(TokenRequiredException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ResponseType.TOKEN_REQUIRED, ResponseType.TOKEN_REQUIRED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongTokenPrefixException.class)
	public ResponseEntity<?> handleWrongTokenPrefixException(WrongTokenPrefixException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ResponseType.WRONG_TOKEN_PREFIX, ResponseType.WRONG_TOKEN_PREFIX.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ResponseType.EXPIRED_TOKEN, ResponseType.EXPIRED_TOKEN.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MalformedJwtException.class)
	public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ResponseType.INVALID_TOKEN, ResponseType.INVALID_TOKEN.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MissingRequestHeaderException.class)
	public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ResponseType.AUTHORIZATION_HEADER_REQUIRED, ResponseType.AUTHORIZATION_HEADER_REQUIRED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = SignatureException.class)
	public ResponseEntity<?> handleSignatureException(SignatureException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ResponseType.INVALID_JWT_SIGNATURE, ResponseType.INVALID_JWT_SIGNATURE.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.INVALID_REQUEST_BODY, ResponseType.INVALID_REQUEST_BODY.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = ChatRoomNotFoundException.class)
	public ResponseEntity<?> handleChatRoomNotFoundException(ChatRoomNotFoundException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.CHATROOM_NOT_FOUND, ResponseType.CHATROOM_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.USER_NOT_FOUND, ResponseType.USER_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = BoardNotFoundException.class)
	public ResponseEntity<?> handleBoardNotFoundException(BoardNotFoundException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ResponseType.BOARD_NOT_FOUND, ResponseType.BOARD_NOT_FOUND.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongHostException.class)
	public ResponseEntity<?> handleWrongHostException(WrongHostException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.WRONG_CHATROOM_HOST, ResponseType.WRONG_CHATROOM_HOST.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongGuestException.class)
	public ResponseEntity<?> handleWrongGuestException(WrongGuestException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.WRONG_CHATROOM_GUEST, ResponseType.WRONG_CHATROOM_GUEST.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = AlreadyApprovedException.class)
	public ResponseEntity<?> handleAlreadyApprovedException(AlreadyApprovedException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ResponseType.ALREADY_APPROVED, ResponseType.ALREADY_APPROVED.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}
}
