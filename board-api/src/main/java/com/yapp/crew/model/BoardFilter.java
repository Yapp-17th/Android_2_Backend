package com.yapp.crew.model;

import com.yapp.crew.dto.request.BoardFilterRequestDto;
import com.yapp.crew.utils.SortingType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFilter {

  private long userId;
  private SortingType sorting;
  List<Long> category;
  List<Long> city;

  public static BoardFilter build(BoardFilterRequestDto boardFilterRequestDto, long userId) {
    BoardFilter boardFilter = new BoardFilter();
    boardFilter.userId = userId;
    boardFilter.sorting = SortingType.getSortingType(boardFilterRequestDto.getSorting());
    boardFilter.category = boardFilterRequestDto.getCategory();
    boardFilter.city = boardFilterRequestDto.getCity();

    return boardFilter;
  }
}
