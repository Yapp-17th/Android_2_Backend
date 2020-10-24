package com.yapp.crew.network;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponseBody<T> {

	private LocalDateTime transactionTime;

	@JsonInclude(NON_NULL)
	private Long firstUnreadMessageId;

	@JsonInclude(NON_NULL)
	private T data;

	public static <T> HttpResponseBody<T> buildChatMessagesResponse(T data, Long firstUnreadMessageId) {
		return (HttpResponseBody<T>) HttpResponseBody.builder()
						.transactionTime(LocalDateTime.now())
						.firstUnreadMessageId(firstUnreadMessageId)
						.data(data)
						.build();
	}
}
