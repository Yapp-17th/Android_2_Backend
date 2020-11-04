package com.yapp.crew.model;

import com.yapp.crew.dto.BoardSearchDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearch {

  private List<String> keywords = new ArrayList<>();

  public static BoardSearch build(BoardSearchDto boardSearchDto) {
    BoardSearch boardSearch = new BoardSearch();
    boardSearch.keywords = Arrays.stream(boardSearchDto.getKeywords().split(" "))
        .collect(Collectors.toList());
    return boardSearch;
  }
}
