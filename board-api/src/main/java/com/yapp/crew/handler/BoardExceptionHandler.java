package com.yapp.crew.handler;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.InactiveUserException;
import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.SuspendedUserException;
import com.yapp.crew.domain.errors.TokenRequiredException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.errors.WrongTokenPrefixException;
import com.yapp.crew.model.SimpleResponse;
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
public class BoardExceptionHandler {

  @ExceptionHandler(value = InactiveUserException.class)
  public ResponseEntity<?> handleInactiveUserException(InactiveUserException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = InternalServerErrorException.class)
  public ResponseEntity<?> handleInternalServerErrorException(InternalServerErrorException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = InvalidRequestBodyException.class)
  public ResponseEntity<?> handleInvalidRequestBodyException(InvalidRequestBodyException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = SuspendedUserException.class)
  public ResponseEntity<?> handleSuspendedUserException(SuspendedUserException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = TokenRequiredException.class)
  public ResponseEntity<?> handleTokenRequiredException(TokenRequiredException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = WrongTokenPrefixException.class)
  public ResponseEntity<?> handleWrongTokenPrefixException(WrongTokenPrefixException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = ExpiredJwtException.class)
  public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MalformedJwtException.class)
  public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MissingRequestHeaderException.class)
  public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = SignatureException.class)
  public ResponseEntity<?> handleSignatureException(SignatureException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = BoardNotFoundException.class)
  public ResponseEntity<?> handleBoardNotFoundException(BoardNotFoundException ex) {
    SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }
}
