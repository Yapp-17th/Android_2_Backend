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

	@JsonInclude(NON_NULL)
	private Long firstUnreadMessageId;

	@JsonInclude(NON_NULL)
	private T data;

	public static <T> HttpResponseBody<T> buildChatMessagesResponse(T data, Integer status, Long firstUnreadMessageId) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
						.transactionTime(LocalDateTime.now())
						.status(status)
						.firstUnreadMessageId(firstUnreadMessageId)
						.data(data)
						.build();
	}

	public static <T> HttpResponseBody<T> buildChatRoomsResponse(T data, Integer status) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
						.transactionTime(LocalDateTime.now())
						.status(status)
						.data(data)
						.build();
	}

	public static <T> HttpResponseBody<T> buildChatRoomResponse(T data, Integer status) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
						.transactionTime(LocalDateTime.now())
						.status(status)
						.data(data)
						.build();
	}

	public static <T> HttpResponseBody<T> buildErrorResponse(Integer status, String message) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
						.transactionTime(LocalDateTime.now())
						.status(status)
						.message(message)
						.build();
	}

	public static <T> HttpResponseBody<T> buildSuccessResponse(Integer status, String message) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
						.transactionTime(LocalDateTime.now())
						.status(status)
						.message(message)
						.build();
	}
}
