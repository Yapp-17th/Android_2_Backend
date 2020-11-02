package com.yapp.crew.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFilterRequestDto {

  @NotNull
  private Integer limit;
  @NotNull
  private Integer offset;
  @NotNull
  private String sorting;
  @NotNull
  List<Integer> category;
  @NotNull
  List<Integer> city;
}
