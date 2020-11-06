package com.yapp.crew.dto.request;

import java.time.LocalDateTime;
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
	private Long category;
	@NotNull
	private Long city;
	@NotNull
	private Long userTag;
	@NotNull
	private Integer recruitNumber;
	@NotNull
	private LocalDateTime date;
	@NotBlank
	private String place;
}
