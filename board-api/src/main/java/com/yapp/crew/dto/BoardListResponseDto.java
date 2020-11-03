package com.yapp.crew.dto;

import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardResponseInfo;
import com.yapp.crew.utils.ResponseMessage;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class BoardListResponseDto {

  private int status = HttpStatus.OK.value();
  private boolean success = true;
  private String message = ResponseMessage.BOARD_CONTENT_SUCCESS.getMessage();
  private List<BoardResponseInfo> data;

  public static BoardListResponseDto build(List<BoardResponseInfo> boardResponseInfoList) {
    BoardListResponseDto boardListResponseDto = new BoardListResponseDto();
    boardListResponseDto.data = boardResponseInfoList;
    return boardListResponseDto;
  }
}
