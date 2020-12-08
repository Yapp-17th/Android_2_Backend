package com.yapp.crew.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardInfoRequestDto {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@NotNull
	private long category;

	@NotNull
	private long city;

	@NotNull
	private long userTag;

	@NotNull
	private int recruitNumber;

	@NotNull
	@FutureOrPresent
	private LocalDateTime date;

	@NotBlank
	private String place;

	@Override
	public String toString() {
		return "BoardInfoRequestDto{" +
				"title='" + title + '\'' +
				", content='" + content + '\'' +
				", category=" + category +
				", city=" + city +
				", userTag=" + userTag +
				", recruitNumber=" + recruitNumber +
				", date=" + date +
				", place='" + place + '\'' +
				'}';
	}
}
