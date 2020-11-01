package com.yapp.crew.exception;

import com.yapp.crew.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InactiveUserException extends RuntimeException {

  public InactiveUserException() {
    super(ResponseMessage.INACTIVE_USER_FAIL.getMessage());
  }

  public InactiveUserException(Throwable cause) {
    super(ResponseMessage.INACTIVE_USER_FAIL.getMessage(), cause);
  }
}
