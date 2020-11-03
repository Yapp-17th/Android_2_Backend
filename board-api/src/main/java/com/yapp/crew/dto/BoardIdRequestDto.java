package com.yapp.crew.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardIdRequestDto {

  @NotNull
  private Long boardId;
}
