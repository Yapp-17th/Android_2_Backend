package com.yapp.crew.domain.errors;

public class TagNotFoundException extends RuntimeException {

	public TagNotFoundException(Long tagId) {
		super("Cannot find tag with id: " + tagId);
	}
}
