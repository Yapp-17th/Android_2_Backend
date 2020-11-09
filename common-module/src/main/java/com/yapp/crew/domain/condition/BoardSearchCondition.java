package com.yapp.crew.domain.condition;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchCondition {

	private Long userId;
	private List<String> keywords;

	public static BoardSearchCondition build(List<String> keyword, Long userId) {
		BoardSearchCondition boardSearch = new BoardSearchCondition();
		boardSearch.userId = userId;
		boardSearch.keywords = keyword;

		return boardSearch;
	}
}
