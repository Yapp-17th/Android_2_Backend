package com.yapp.crew.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchRequestDto {

  @NotBlank
  private String keywords;
}
