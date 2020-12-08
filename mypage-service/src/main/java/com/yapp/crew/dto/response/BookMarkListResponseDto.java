package com.yapp.crew.dto.response;

import com.yapp.crew.model.BoardListInfo;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class BookMarkListResponseDto {

	private int status;
	private boolean success;
	private String message;
	private List<BoardListInfo> data;

	public static BookMarkListResponseDtoBuilder getBuilder() {
		return new BookMarkListResponseDtoBuilder();
	}

	public static class BookMarkListResponseDtoBuilder {
		private int status = HttpStatus.OK.value();
		private boolean success = true;
		private String message = "신청자 리스트 조회 성공";
		private List<BoardListInfo> data = Collections.emptyList();

		public BookMarkListResponseDtoBuilder withStatus(int status) {
			this.status = status;
			return this;
		}

		public BookMarkListResponseDtoBuilder withSuccess(boolean success) {
			this.success = success;
			return this;
		}

		public BookMarkListResponseDtoBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public BookMarkListResponseDtoBuilder withData(List<BoardListInfo> data) {
			this.data = data;
			return this;
		}

		public BookMarkListResponseDto build() {
			BookMarkListResponseDto bookMarkListResponseDto = new BookMarkListResponseDto();
			bookMarkListResponseDto.setStatus(status);
			bookMarkListResponseDto.setSuccess(success);
			bookMarkListResponseDto.setMessage(message);
			bookMarkListResponseDto.setData(data);
			return bookMarkListResponseDto;
		}
	}

	public static BookMarkListResponseDto build(List<BoardListInfo> data) {
		return BookMarkListResponseDto.getBuilder().withData(data).build();
	}
}
