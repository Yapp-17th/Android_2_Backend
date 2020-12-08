package com.yapp.crew.model;

import com.yapp.crew.domain.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCode {

	private long id;
	private String name;

	public static CategoryCode build(Category category) {
		CategoryCode categoryCode = new CategoryCode();
		categoryCode.id = category.getId();
		categoryCode.name = category.getExercise().getName();

		return categoryCode;
	}
}
