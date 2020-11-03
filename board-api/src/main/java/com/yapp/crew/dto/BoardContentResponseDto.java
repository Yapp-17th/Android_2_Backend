package com.yapp.crew.dto;

import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.utils.ResponseMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class BoardContentResponseDto {

  private int status = HttpStatus.OK.value();
  private boolean success = true;
  private String message = ResponseMessage.BOARD_CONTENT_SUCCESS.getMessage();
  private BoardContentResponseInfo data;

  public static BoardContentResponseDto build(BoardContentResponseInfo boardContentResponseInfo) {
    BoardContentResponseDto boardContentResponseDto = new BoardContentResponseDto();
    boardContentResponseDto.data = boardContentResponseInfo;
    return boardContentResponseDto;
  }
}
