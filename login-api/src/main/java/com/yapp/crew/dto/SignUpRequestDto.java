package com.yapp.crew.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignUpRequestDto {

  @NotNull
  private String userId;
  @NotNull
  private String userName;
  @NotNull
  private String nickName;
  @NotNull
  private String email;
  @NotNull
  private String accessToken;
  @NotNull
  private long category;
  @NotNull
  private long address;
  @NotNull
  private String intro;
}
