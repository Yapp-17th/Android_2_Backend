package com.yapp.crew.dto.response;

import com.yapp.crew.model.EvaluateListInfo;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class EvaluateListResponseDto {

	private int status ;
	private boolean success;
	private String message;
	private List<EvaluateListInfo> data;

	public static EvaluateListResponseDtoBuilder getBuilder() {
		return new EvaluateListResponseDtoBuilder();
	}

	public static class EvaluateListResponseDtoBuilder {
		private int status = HttpStatus.OK.value();
		private boolean success = true;
		private String message = "평가 리스트 조회 성공";
		private List<EvaluateListInfo> data = Collections.emptyList();

		public EvaluateListResponseDtoBuilder withStatus(int status) {
			this.status = status;
			return this;
		}

		public EvaluateListResponseDtoBuilder withSuccess(boolean success) {
			this.success = success;
			return this;
		}

		public EvaluateListResponseDtoBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public EvaluateListResponseDtoBuilder withData(List<EvaluateListInfo> data) {
			this.data = data;
			return this;
		}

		public EvaluateListResponseDto build() {
			EvaluateListResponseDto evaluateListResponseDto = new EvaluateListResponseDto();
			evaluateListResponseDto.setStatus(status);
			evaluateListResponseDto.setSuccess(success);
			evaluateListResponseDto.setMessage(message);
			evaluateListResponseDto.setData(data);
			return evaluateListResponseDto;
		}
	}

	public static EvaluateListResponseDto build(List<EvaluateListInfo> data) {
		return EvaluateListResponseDto.getBuilder().withData(data).build();
	}
}
