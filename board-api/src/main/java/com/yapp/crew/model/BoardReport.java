package com.yapp.crew.model;

import com.yapp.crew.domain.type.ReportType;
import com.yapp.crew.dto.request.BoardReportRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardReport {

  private long boardId;
  private long reporter;
  private ReportType reportType;
  private String content;

  public static BoardReport build(BoardReportRequestDto boardReportRequestDto, long userId) {
    BoardReport boardReport = new BoardReport();
    boardReport.boardId = boardReportRequestDto.getBoardId();
    boardReport.reporter = userId;
    boardReport.reportType = ReportType.values()[Math.toIntExact(boardReportRequestDto.getReportType())];
    boardReport.content = boardReportRequestDto.getContent();

    return boardReport;
  }
}
