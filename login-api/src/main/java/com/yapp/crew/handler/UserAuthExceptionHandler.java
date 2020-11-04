package com.yapp.crew.handler;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.InactiveUserException;
import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.SuspendedUserException;
import com.yapp.crew.domain.errors.TokenRequiredException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.errors.WrongTokenPrefixException;
import com.yapp.crew.model.UserAuthResponseBody;
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
  public ResponseEntity<?> handleTokenRequiredException(InactiveUserException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = InternalServerErrorException.class)
  public ResponseEntity<?> handleTokenRequiredException(InternalServerErrorException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = InvalidRequestBodyException.class)
  public ResponseEntity<?> handleTokenRequiredException(InvalidRequestBodyException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.BAD_REQUEST, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = SuspendedUserException.class)
  public ResponseEntity<?> handleTokenRequiredException(SuspendedUserException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }


  @ExceptionHandler(value = TokenRequiredException.class)
  public ResponseEntity<?> handleTokenRequiredException(TokenRequiredException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = WrongTokenPrefixException.class)
  public ResponseEntity<?> handleWrongTokenPrefixException(WrongTokenPrefixException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = ExpiredJwtException.class)
  public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MalformedJwtException.class)
  public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MissingRequestHeaderException.class)
  public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = SignatureException.class)
  public ResponseEntity<?> handleSignatureException(SignatureException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.BAD_REQUEST, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.NOT_FOUND, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }

  @ExceptionHandler(value = BoardNotFoundException.class)
  public ResponseEntity<?> handleBoardNotFoundException(BoardNotFoundException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.NOT_FOUND, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }
}
