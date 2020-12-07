package com.yapp.crew.domain.condition;

import com.yapp.crew.domain.type.SortingType;
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

	public static BoardFilterCondition build(String sorting, List<Long> category, List<Long> city, long userId) {
		BoardFilterCondition boardFilter = new BoardFilterCondition();
		boardFilter.userId = userId;
		boardFilter.sorting = SortingType.getSortingType(sorting);
		boardFilter.category = category;
		boardFilter.city = city;

		return boardFilter;
	}
}
