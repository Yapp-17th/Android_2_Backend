package com.yapp.crew.exception;

import com.yapp.crew.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class WrongTokenException extends RuntimeException {

  public WrongTokenException() {
    super(ResponseMessage.TOKEN_NOT_VALID.getMessage());
  }

  public WrongTokenException(Throwable cause) {
    super(ResponseMessage.TOKEN_NOT_VALID.getMessage(), cause);
  }
}
