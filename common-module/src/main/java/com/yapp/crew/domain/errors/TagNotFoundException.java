package com.yapp.crew.domain.errors;

public class TagNotFoundException extends RuntimeException {

	public TagNotFoundException(long tagId) {
		super("Cannot find tag with id: " + tagId);
	}
}
