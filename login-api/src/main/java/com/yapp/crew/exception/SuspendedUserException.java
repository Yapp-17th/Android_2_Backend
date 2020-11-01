package com.yapp.crew.exception;

import com.yapp.crew.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class SuspendedUserException extends RuntimeException {

  public SuspendedUserException() {
    super(ResponseMessage.SUSPENDED_USER_FAIL.getMessage());
  }

  public SuspendedUserException(Throwable cause) {
    super(ResponseMessage.SUSPENDED_USER_FAIL.getMessage(), cause);
  }
}
