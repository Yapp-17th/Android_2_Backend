package com.yapp.crew.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFilterRequestDto {

  private String sorting = "LATEST";
  List<Long> category = null;
  List<Long> city = null;
}
