package com.yapp.crew.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearch {

	private long userId;
	private List<String> keywords;

	public static BoardSearch build(List<String> keyword, long userId) {
		BoardSearch boardSearch = new BoardSearch();
		boardSearch.userId = userId;
		boardSearch.keywords = keyword;

		return boardSearch;
	}
}
