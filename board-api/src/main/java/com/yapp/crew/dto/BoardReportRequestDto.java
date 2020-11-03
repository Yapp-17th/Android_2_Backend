package com.yapp.crew.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardReportRequestDto {

  @NotNull
  private Long boardId;
  @NotNull
  private Long reportType;
  private String content;
}
