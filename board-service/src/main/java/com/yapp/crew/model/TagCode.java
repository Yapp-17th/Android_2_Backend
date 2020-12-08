package com.yapp.crew.model;

import com.yapp.crew.domain.model.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagCode {

	private long id;
	private String name;

	public static TagCode build(Tag tag) {
		TagCode tagCode = new TagCode();
		tagCode.id = tag.getId();
		tagCode.name = tag.getName().getName();

		return tagCode;
	}

	public static TagCode build(long id, String name) {
		TagCode tagCode = new TagCode();
		tagCode.id = id;
		tagCode.name = name;

		return tagCode;
	}
}
