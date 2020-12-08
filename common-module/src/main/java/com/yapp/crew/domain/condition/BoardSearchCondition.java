
package com.yapp.crew.domain.condition;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchCondition {

	private Long userId;
	private List<String> keywords;
	private Date timestamp;

	public static BoardSearchCondition build(List<String> keyword, Long userId, Date timestamp) {
		BoardSearchCondition boardSearch = new BoardSearchCondition();
		boardSearch.userId = userId;
		boardSearch.keywords = keyword;
		boardSearch.timestamp = timestamp;

		return boardSearch;
	}
}
