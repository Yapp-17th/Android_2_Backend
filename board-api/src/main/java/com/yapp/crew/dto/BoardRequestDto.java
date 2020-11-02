package com.yapp.crew.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

  @NotNull
  private String title;
  @NotNull
  private String content;
  @NotNull
  private Long category;
  @NotNull
  private Long city;
  @NotNull
  private Long userTag;
  @NotNull
  private Integer recruitNumber;
  @NotNull
  private LocalDateTime date;
  @NotNull
  private String place;
}
