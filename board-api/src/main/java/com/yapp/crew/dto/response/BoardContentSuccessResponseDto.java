package com.yapp.crew.dto.response;

import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.utils.ResponseMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class BoardContentSuccessResponseDto {

  private final int status = HttpStatus.OK.value();
  private final boolean success = true;
  private final String message = ResponseMessage.BOARD_CONTENT_SUCCESS.getMessage();
  private BoardContentResponseInfo data;

  public static BoardContentSuccessResponseDto build(BoardContentResponseInfo boardContentResponseInfo) {
    BoardContentSuccessResponseDto boardContentSuccessResponseDto = new BoardContentSuccessResponseDto();
    boardContentSuccessResponseDto.data = boardContentResponseInfo;
    return boardContentSuccessResponseDto;
  }
}
