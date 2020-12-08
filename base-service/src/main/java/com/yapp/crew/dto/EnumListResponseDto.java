package com.yapp.crew.dto;

import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.EnumData;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class EnumListResponseDto {

	private String type;
	private int status;
	private ResponseType message;
	private List<EnumData> data;

	public static EnumListResponseDto pass(String type, List<String> data) {
		EnumListResponseDto enumListResponseDto = new EnumListResponseDto();
		enumListResponseDto.type = type;
		enumListResponseDto.status = HttpStatus.OK.value();
		enumListResponseDto.message = ResponseType.SUCCESS;
		enumListResponseDto.setData(data);
		return enumListResponseDto;
	}

	public void setData(List<String> data) {
		List<EnumData> enumData = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			enumData.add(new EnumData(i + 1, data.get(i)));
		}
		this.data = enumData;
	}
}
