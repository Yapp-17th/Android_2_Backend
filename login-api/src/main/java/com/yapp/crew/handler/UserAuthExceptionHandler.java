package com.yapp.crew.handler;

import com.yapp.crew.exception.InactiveUserException;
import com.yapp.crew.exception.InternalServerErrorException;
import com.yapp.crew.exception.InvalidRequestBodyException;
import com.yapp.crew.exception.SuspendedUserException;
import com.yapp.crew.exception.WrongTokenException;
import com.yapp.crew.model.UserAuthResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @ExceptionHandler(value = WrongTokenException.class)
  public ResponseEntity<?> handleTokenRequiredException(WrongTokenException ex) {
    UserAuthResponseBody responseBody = UserAuthResponseBody.fail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
  }
}
