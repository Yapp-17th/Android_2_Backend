package com.yapp.crew.model;

import com.yapp.crew.dto.BoardFilterRequestDto;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFilter {

  private String sorting;
  List<Long> category;
  List<Long> city;

  public BoardFilter(BoardFilterRequestDto boardFilterRequestDto) {
    BoardFilter boardFilter = new BoardFilter();
    boardFilter.sorting = boardFilterRequestDto.getSorting();
    boardFilter.category = boardFilterRequestDto.getCategory();
    boardFilter.city = boardFilterRequestDto.getCity();
  }
}
