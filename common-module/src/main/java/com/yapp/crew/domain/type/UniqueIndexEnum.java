package com.yapp.crew.domain.type;

public enum UniqueIndexEnum {
	EMAIL("동일한 이메일이 있습니다."),
	ACCESS_TOKEN("이미 가입한 유저입니다."),
	NICKNAME("동일한 닉네임이 있습니다."),
	OAUTH_ID("이미 가입한 유저입니다.");

	private final String name;

	UniqueIndexEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
