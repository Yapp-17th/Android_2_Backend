package com.yapp.crew.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.EnumData;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class EnumListDto {

  private String type;
  private int status;
  private ResponseType message;
  private List<EnumData> data;

  public static EnumListDto pass(String type, List<String> data) {
    EnumListDto enumListDto = new EnumListDto();
    enumListDto.type = type;
    enumListDto.status = HttpStatus.OK.value();
    enumListDto.message = ResponseType.SUCCESS;
    enumListDto.setData(data);
    return enumListDto;
  }

  public void setData(List<String> data) {
    List<EnumData> enumData = new ArrayList<>();
    for (int i = 0; i < data.size(); i++) {
      enumData.add(new EnumData(i + 1, data.get(i)));
    }
    this.data = enumData;
  }
}
