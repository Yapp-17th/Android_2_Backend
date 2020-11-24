package com.yapp.crew.domain.errors;

public class CategoryNotFoundException extends RuntimeException {

	public CategoryNotFoundException(Long categoryId) {
		super("Cannot find category with id: " + categoryId);
	}
}
