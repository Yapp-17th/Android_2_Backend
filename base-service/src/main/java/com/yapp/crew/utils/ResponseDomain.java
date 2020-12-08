package com.yapp.crew.utils;

public enum ResponseDomain {
	ADDRESS_CITY("city"),
	EXERCISE("exercise"),
	USER_TAG("userTag");

	private final String name;

	ResponseDomain(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
