package com.yapp.crew.exception;

import com.yapp.crew.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

  public InternalServerErrorException() {
    super(ResponseMessage.INTERNAL_SERVER_FAIL.getMessage());
  }

  public InternalServerErrorException(Throwable cause) {
    super(ResponseMessage.INTERNAL_SERVER_FAIL.getMessage(), cause);
  }
}
