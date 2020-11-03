package com.yapp.crew.model;

import com.yapp.crew.dto.BoardSearchDto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearch {

  private List<String> keywords = new ArrayList<>();

  public BoardSearch(BoardSearchDto boardSearchDto) {
    StringTokenizer stringTokenizer = new StringTokenizer(boardSearchDto.getKeywords());
    while (stringTokenizer.hasMoreTokens()) {
      String keyword = stringTokenizer.nextToken();
      if (!keywords.contains(keyword)) {
        keywords.add(stringTokenizer.nextToken());
      }
    }
  }
}
