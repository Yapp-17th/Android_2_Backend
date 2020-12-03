package com.yapp.crew.dto.response;

import com.yapp.crew.model.ApplyListInfo;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ApplyListResponseDto {

	private Integer status;
	private Boolean success;
	private String message;
	private List<ApplyListInfo> data;

	public static ApplyListResponseDtoBuilder getBuilder() {
		return new ApplyListResponseDtoBuilder();
	}

	public static class ApplyListResponseDtoBuilder {
		private Integer status = HttpStatus.OK.value();
		private Boolean success = true;
		private String message = "신청자 리스트 조회 성공";
		private List<ApplyListInfo> data = Collections.emptyList();

		public ApplyListResponseDtoBuilder withStatus(Integer status) {
			this.status = status;
			return this;
		}

		public ApplyListResponseDtoBuilder withSuccess(Boolean success) {
			this.success = success;
			return this;
		}

		public ApplyListResponseDtoBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public ApplyListResponseDtoBuilder withData(List<ApplyListInfo> data) {
			this.data = data;
			return this;
		}

		public ApplyListResponseDto build() {
			ApplyListResponseDto applyListResponseDto = new ApplyListResponseDto();
			applyListResponseDto.setStatus(status);
			applyListResponseDto.setSuccess(success);
			applyListResponseDto.setMessage(message);
			applyListResponseDto.setData(data);
			return applyListResponseDto;
		}
	}

	public static ApplyListResponseDto build(List<ApplyListInfo> data) {
		return ApplyListResponseDto.getBuilder().withData(data).build();
	}
}
