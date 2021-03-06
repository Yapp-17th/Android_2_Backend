package com.yapp.crew.network.model;

import com.yapp.crew.domain.type.ResponseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class SimpleResponse {

	private int status;
	private boolean isSuccess;
	private ResponseType responseType;
	private String message;

	public static SimpleResponse pass(ResponseType responseType) {
		SimpleResponse simpleResponse = new SimpleResponse();
		simpleResponse.status = HttpStatus.OK.value();
		simpleResponse.isSuccess = true;
		simpleResponse.responseType = responseType;
		simpleResponse.message = responseType.getMessage();
		return simpleResponse;
	}

	public static SimpleResponse fail(HttpStatus httpStatus, ResponseType responseType) {
		SimpleResponse simpleResponse = new SimpleResponse();
		simpleResponse.status = httpStatus.value();
		simpleResponse.isSuccess = false;
		simpleResponse.responseType = responseType;
		simpleResponse.message = responseType.getMessage();
		return simpleResponse;
	}

	public static SimpleResponse fail(HttpStatus httpStatus, ResponseType responseType, String message) {
		SimpleResponse simpleResponse = new SimpleResponse();
		simpleResponse.status = httpStatus.value();
		simpleResponse.isSuccess = false;
		simpleResponse.responseType = responseType;
		simpleResponse.message = message;
		return simpleResponse;
	}
}
