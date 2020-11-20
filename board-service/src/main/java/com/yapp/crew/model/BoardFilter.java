package com.yapp.crew.model;

import com.yapp.crew.utils.SortingType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFilter {

	private Long userId;
	private SortingType sorting;
	List<Long> category;
	List<Long> city;

	public static BoardFilter build(String sorting, List<Long> category, List<Long> city, long userId) {
		BoardFilter boardFilter = new BoardFilter();
		boardFilter.userId = userId;
		boardFilter.sorting = SortingType.getSortingType(sorting);
		boardFilter.category = category;
		boardFilter.city = city;

		return boardFilter;
	}
}
