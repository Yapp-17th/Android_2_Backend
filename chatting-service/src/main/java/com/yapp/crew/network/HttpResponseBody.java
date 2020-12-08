package com.yapp.crew.network;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.status.AppliedStatus;
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

	private int status;

	private ResponseType responseType;

	@JsonInclude(NON_NULL)
	private String message;

	@JsonInclude(NON_NULL)
	private long firstUnreadMessageId;

	@JsonInclude(NON_NULL)
	private String boardTitle;

	@JsonInclude(NON_NULL)
	private AppliedStatus appliedStatus;

	@JsonInclude(NON_NULL)
	private boolean isHostExited;

	@JsonInclude(NON_NULL)
	private boolean isGuestExited;

	@JsonInclude(NON_NULL)
	private T data;

	public static <T> HttpResponseBody<T> buildChatMessagesResponse(
			T data, int status, ResponseType responseType, long firstUnreadMessageId, String boardTitle, AppliedStatus appliedStatus, ChatRoom chatRoom
	) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.firstUnreadMessageId(firstUnreadMessageId)
				.boardTitle(boardTitle)
				.appliedStatus(appliedStatus)
				.isHostExited(chatRoom.isHostExited())
				.isGuestExited(chatRoom.isGuestExited())
				.data(data)
				.build();
	}

	public static <T> HttpResponseBody<T> buildChatRoomsResponse(T data, int status, ResponseType responseType) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.data(data)
				.build();
	}

	public static <T> HttpResponseBody<T> buildChatRoomResponse(T data, int status, ResponseType responseType) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.data(data)
				.build();
	}

	public static <T> HttpResponseBody<T> buildSuccessResponse(int status, ResponseType responseType, String message) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.message(message)
				.build();
	}

	public static <T> HttpResponseBody<T> buildSuccessResponse(int status, ResponseType responseType, String message, T data) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
				.transactionTime(LocalDateTime.now())
				.status(status)
				.responseType(responseType)
				.message(message)
				.data(data)
				.build();
	}
}
