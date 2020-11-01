package com.yapp.crew.exception;

import com.yapp.crew.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestBodyException extends RuntimeException {

  public InvalidRequestBodyException(String errorMessage) {
    super(errorMessage);
  }

  public InvalidRequestBodyException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
