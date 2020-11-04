package com.yapp.crew.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFilterRequestDto {

  private String sorting = "latest";
  List<Long> category = null;
  List<Long> city = null;
}
