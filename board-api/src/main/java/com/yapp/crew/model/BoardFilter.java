package com.yapp.crew.model;

import com.yapp.crew.dto.BoardFilterRequestDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFilter {

  private long userId;
  private String sorting;
  List<Long> category;
  List<Long> city;

  public BoardFilter(BoardFilterRequestDto boardFilterRequestDto, long userId) {
    BoardFilter boardFilter = new BoardFilter();
    boardFilter.userId = userId;
    boardFilter.sorting = boardFilterRequestDto.getSorting();
    boardFilter.category = boardFilterRequestDto.getCategory();
    boardFilter.city = boardFilterRequestDto.getCity();
  }
}
