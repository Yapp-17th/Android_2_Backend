package com.yapp.crew.dto;

import com.yapp.crew.model.BoardResponseInfo;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardListResponseDto {

  private int status;
  private boolean success;
  private String message;
  private List<BoardResponseInfo> data;
}
