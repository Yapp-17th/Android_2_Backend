package com.yapp.crew.dto;

import com.yapp.crew.config.ResponseMessage;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class EnumListSuccessDto {

  private String type;
  private int status;
  private String message;
  private List<String> data;

  public static EnumListSuccessDto builder(String type) {
    EnumListSuccessDto enumListSuccessDto = new EnumListSuccessDto();
    enumListSuccessDto.type = type;
    enumListSuccessDto.status = HttpStatus.OK.value();
    enumListSuccessDto.message = ResponseMessage.SUCCESS.getMessage();
    return enumListSuccessDto;
  }

  public void setData(List<String> data) {
    this.data = data;
  }
}
