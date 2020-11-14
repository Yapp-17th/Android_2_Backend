package com.yapp.crew.errors;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;

public class InactiveUserException extends ZuulException {

	public InactiveUserException(String message) {
		super(message, HttpStatus.FORBIDDEN.value(), "InactiveUserException");
	}
}
