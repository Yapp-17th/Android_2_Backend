package com.yapp.crew.domain.condition;

import com.yapp.crew.domain.type.SortingType;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFilterCondition {

	private long userId;
	private SortingType sorting = SortingType.LATEST;
	List<Long> category;
	List<Long> city;
	Date timestamp;

	public static BoardFilterCondition build(String sorting, List<Long> category, List<Long> city, long userId, Date timestamp) {
		BoardFilterCondition boardFilter = new BoardFilterCondition();
		boardFilter.userId = userId;
		boardFilter.sorting = SortingType.getSortingType(sorting);
		boardFilter.category = category;
		boardFilter.city = city;
		boardFilter.timestamp = timestamp;

		return boardFilter;
	}
}
