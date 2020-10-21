package com.yapp.crew.dto;

import com.yapp.crew.config.ResponseMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class EnumListFailDto {

  private String type;
  private int status;
  private String message;

  public static EnumListFailDto builder(String type) {
    EnumListFailDto enumListFailDto = new EnumListFailDto();
    enumListFailDto.type = type;
    enumListFailDto.status = HttpStatus.BAD_REQUEST.value();
    enumListFailDto.message = ResponseMessage.FAIL.getMessage();
    return enumListFailDto;
  }

  public void addMessage(String message) {
    this.message = String.join(",", this.message, message);
  }
}
