package com.yapp.crew.errors;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;

public class SuspendedUserException extends ZuulException {

	public SuspendedUserException(String message) {
		super(message, HttpStatus.UNAUTHORIZED.value(), "SuspendedUserException");
	}
}
