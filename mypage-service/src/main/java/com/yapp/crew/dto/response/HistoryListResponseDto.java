package com.yapp.crew.dto.response;

import com.yapp.crew.model.HistoryListInfo;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class HistoryListResponseDto {

	private Integer status;
	private Boolean success;
	private String message;
	private String type;
	private List<HistoryListInfo> data;

	public static HistoryListResponseDtoBuilder getBuilder() {
		return new HistoryListResponseDtoBuilder();
	}

	public static class HistoryListResponseDtoBuilder {
		private Integer status = HttpStatus.OK.value();
		private Boolean success = true;
		private String message = "히스토리 조회 성공";
		private String type = "";
		private List<HistoryListInfo> data = Collections.emptyList();

		public HistoryListResponseDtoBuilder withStatus(Integer status) {
			this.status = status;
			return this;
		}

		public HistoryListResponseDtoBuilder withSuccess(Boolean success) {
			this.success = success;
			return this;
		}

		public HistoryListResponseDtoBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public HistoryListResponseDtoBuilder withType(String type) {
			this.type = type;
			return this;
		}

		public HistoryListResponseDtoBuilder withData(List<HistoryListInfo> data) {
			this.data = data;
			return this;
		}

		public HistoryListResponseDto build() {
			HistoryListResponseDto historyListResponseDto = new HistoryListResponseDto();
			historyListResponseDto.setStatus(status);
			historyListResponseDto.setSuccess(success);
			historyListResponseDto.setMessage(message);
			historyListResponseDto.setType(type);
			historyListResponseDto.setData(data);
			return historyListResponseDto;
		}
	}

	public static HistoryListResponseDto build(String type, List<HistoryListInfo> data) {
		return HistoryListResponseDto.getBuilder().withType(type).withData(data).build();
	}
}
