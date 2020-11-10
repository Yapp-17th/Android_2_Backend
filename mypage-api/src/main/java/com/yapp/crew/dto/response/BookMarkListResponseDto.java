package com.yapp.crew.dto.response;

import com.yapp.crew.model.BoardListInfo;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class BookMarkListResponseDto {

	private int status = HttpStatus.OK.value();
	private boolean success = true;
	private String message = "북마크 조회 성공";
	private List<BoardListInfo> data;

	public static BookMarkListResponseDto build(List<BoardListInfo> data) {
		BookMarkListResponseDto bookMarkListResponseDto = new BookMarkListResponseDto();
		bookMarkListResponseDto.data = data;

		return bookMarkListResponseDto;
	}
}
