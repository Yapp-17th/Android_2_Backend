package com.yapp.crew.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchDto {

  @NotBlank
  private String keywords;
}
