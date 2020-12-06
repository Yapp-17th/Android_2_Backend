package com.yapp.crew.model;

import com.yapp.crew.domain.model.Tag;
import com.yapp.crew.domain.type.UserTag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagCode {

	private Long id;
	private String name;

	public static TagCode build(Tag tag) {
		TagCode tagCode = new TagCode();
		tagCode.id = tag.getId();
		tagCode.name = tag.getName().getName();

		return tagCode;
	}

	public static TagCode build(Long id, String name) {
		TagCode tagCode = new TagCode();
		tagCode.id = id;
		tagCode.name = name;

		return tagCode;
	}
}
