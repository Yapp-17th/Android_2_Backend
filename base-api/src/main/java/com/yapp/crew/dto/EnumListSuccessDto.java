package com.yapp.crew.dto;

import com.yapp.crew.model.EnumData;
import com.yapp.crew.utils.ResponseMessage;
import java.util.LinkedList;
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
  private List<EnumData> data;

  public static EnumListSuccessDto builder(String type) {
    EnumListSuccessDto enumListSuccessDto = new EnumListSuccessDto();
    enumListSuccessDto.type = type;
    enumListSuccessDto.status = HttpStatus.OK.value();
    enumListSuccessDto.message = ResponseMessage.SUCCESS.getMessage();
    return enumListSuccessDto;
  }

  public void setData(List<String> data) {
    LinkedList<EnumData> enumData = new LinkedList<EnumData>();
    for (int i = 0; i < data.size(); i++) {
      enumData.add(new EnumData(i + 1, data.get(i)));
    }
    this.data = enumData;
  }
}
