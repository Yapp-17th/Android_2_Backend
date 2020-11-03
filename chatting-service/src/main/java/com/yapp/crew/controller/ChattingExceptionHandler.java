package com.yapp.crew.controller;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.TokenRequiredException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.errors.WrongGuestException;
import com.yapp.crew.domain.errors.WrongHostException;
import com.yapp.crew.domain.errors.WrongTokenPrefixException;
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
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongTokenPrefixException.class)
	public ResponseEntity<?> handleWrongTokenPrefixException(WrongTokenPrefixException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MalformedJwtException.class)
	public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MissingRequestHeaderException.class)
	public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = SignatureException.class)
	public ResponseEntity<?> handleSignatureException(SignatureException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = ChatRoomNotFoundException.class)
	public ResponseEntity<?> handleChatRoomNotFoundException(ChatRoomNotFoundException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = BoardNotFoundException.class)
	public ResponseEntity<?> handleBoardNotFoundException(BoardNotFoundException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongHostException.class)
	public ResponseEntity<?> handleWrongHostException(WrongHostException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}

	@ExceptionHandler(value = WrongGuestException.class)
	public ResponseEntity<?> handleWrongGuestException(WrongGuestException ex) {
		HttpResponseBody<?> responseBody = HttpResponseBody.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
	}
}
