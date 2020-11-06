package com.yapp.crew.handler;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.InactiveUserException;
import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.SuspendedUserException;
import com.yapp.crew.domain.errors.TokenRequiredException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.errors.WrongTokenPrefixException;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.network.dto.SimpleResponseDto;
import com.yapp.crew.network.model.SimpleResponse;
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
public class UserAuthExceptionHandler {

  @ExceptionHandler(value = InactiveUserException.class)
  public ResponseEntity<?> handleInactiveUserException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.UNAUTHORIZED, ResponseType.INACTIVE_USER_FAIL);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = InternalServerErrorException.class)
  public ResponseEntity<?> handleInternalServerErrorException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = InvalidRequestBodyException.class)
  public ResponseEntity<?> handleInvalidRequestBodyException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_REQUEST_BODY);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = SuspendedUserException.class)
  public ResponseEntity<?> handleSuspendedUserException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.SUSPENDED_USER_FAIL);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = TokenRequiredException.class)
  public ResponseEntity<?> handleTokenRequiredException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.TOKEN_REQUIRED);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = WrongTokenPrefixException.class)
  public ResponseEntity<?> handleWrongTokenPrefixException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.WRONG_TOKEN_PREFIX);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = ExpiredJwtException.class)
  public ResponseEntity<?> handleExpiredJwtException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.EXPIRED_TOKEN);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MalformedJwtException.class)
  public ResponseEntity<?> handleMalformedJwtException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.WRONG_TOKEN_PREFIX);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MissingRequestHeaderException.class)
  public ResponseEntity<?> handleMissingRequestHeaderException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.AUTHORIZATION_HEADER_REQUIRED);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = SignatureException.class)
  public ResponseEntity<?> handleSignatureException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INVALID_JWT_SIGNATURE);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_METHOD);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFoundException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.USER_NOT_FOUND);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = BoardNotFoundException.class)
  public ResponseEntity<?> handleBoardNotFoundException() {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.BOARD_NOT_FOUND);
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }
}
