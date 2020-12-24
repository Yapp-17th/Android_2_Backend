
package com.yapp.crew.domain.condition;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchCondition {

	private long userId;
	private List<String> keywords;

	public static BoardSearchCondition build(List<String> keyword, long userId) {
		BoardSearchCondition boardSearch = new BoardSearchCondition();
		boardSearch.userId = userId;
		boardSearch.keywords = keyword.stream()
				.map(key ->  URLDecoder.decode(key, StandardCharsets.UTF_8))
				.collect(Collectors.toList());

		return boardSearch;
	}
}
