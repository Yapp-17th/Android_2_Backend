package com.yapp.crew.network;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponseBody<T> {

	private LocalDateTime transactionTime;

	private Integer status;

	@JsonInclude(NON_NULL)
	private String message;

	public static <T> HttpResponseBody<T> buildErrorResponse(Integer status, String message) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
						.transactionTime(LocalDateTime.now())
						.status(status)
						.message(message)
						.build();
	}
}
