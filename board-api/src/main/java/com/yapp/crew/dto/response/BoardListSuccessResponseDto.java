package com.yapp.crew.dto.response;

import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.utils.ResponseMessage;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class BoardListSuccessResponseDto {

  private final int status = HttpStatus.OK.value();
  private final boolean success = true;
  private final String message = ResponseMessage.BOARD_CONTENT_SUCCESS.getMessage();
  private List<BoardListResponseInfo> data;

  public static BoardListSuccessResponseDto build(List<BoardListResponseInfo> boardListResponseInfoList) {
    BoardListSuccessResponseDto boardListSuccessResponseDto = new BoardListSuccessResponseDto();
    boardListSuccessResponseDto.data = boardListResponseInfoList;
    return boardListSuccessResponseDto;
  }
}
