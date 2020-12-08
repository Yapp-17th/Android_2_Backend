package com.yapp.crew.domain.errors;

public class CategoryNotFoundException extends RuntimeException {

	public CategoryNotFoundException(long categoryId) {
		super("Cannot find category with id: " + categoryId);
	}
}
