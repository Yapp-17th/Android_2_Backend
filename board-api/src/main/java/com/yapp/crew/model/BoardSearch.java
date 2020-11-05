package com.yapp.crew.model;

import com.yapp.crew.dto.request.BoardSearchRequestDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearch {

  private Long userId;
  private List<String> keywords = new ArrayList<>();

  public static BoardSearch build(BoardSearchRequestDto boardSearchRequestDto, Long userId) {
    BoardSearch boardSearch = new BoardSearch();
    boardSearch.userId = userId;
    boardSearch.keywords = Arrays.stream(boardSearchRequestDto.getKeywords().split(" "))
        .collect(Collectors.toList());
    return boardSearch;
  }
}
