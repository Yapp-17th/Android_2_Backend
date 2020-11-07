package com.yapp.crew.network;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yapp.crew.domain.type.ResponseType;
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

	private ResponseType responseType;

	@JsonInclude(NON_NULL)
	private String message;

	@JsonInclude(NON_NULL)
	private Long firstUnreadMessageId;

	@JsonInclude(NON_NULL)
	private String boardTitle;

	@JsonInclude(NON_NULL)
	private Boolean isApplied;

	@JsonInclude(NON_NULL)
	private T data;

	public static <T> HttpResponseBody<T> buildChatMessagesResponse(T data, Integer status,
			ResponseType responseType, Long firstUnreadMessageId, String boardTitle, Boolean isApplied) {

		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.firstUnreadMessageId(firstUnreadMessageId)
				.boardTitle(boardTitle)
				.isApplied(isApplied)
				.data(data)
				.build();
	}

	public static <T> HttpResponseBody<T> buildChatMessagesResponse(T data, Integer status,
			ResponseType responseType, Long firstUnreadMessageId) {

		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.firstUnreadMessageId(firstUnreadMessageId)
				.data(data)
				.build();
	}

	public static <T> HttpResponseBody<T> buildChatRoomsResponse(T data, Integer status,
			ResponseType responseType) {

		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.data(data)
				.build();
	}

	public static <T> HttpResponseBody<T> buildChatRoomResponse(T data, Integer status,
			ResponseType responseType) {

		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.data(data)
				.build();
	}

	public static <T> HttpResponseBody<T> buildErrorResponse(Integer status,
			ResponseType responseType, String message) {

		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.message(message)
				.build();
	}

	public static <T> HttpResponseBody<T> buildSuccessResponse(Integer status,
			ResponseType responseType, String message) {

		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.message(message)
				.build();
	}

	public static <T> HttpResponseBody<T> buildSuccessResponse(Integer status,
			ResponseType responseType, String message, T data) {

		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.message(message)
				.data(data)
				.build();
	}
}
