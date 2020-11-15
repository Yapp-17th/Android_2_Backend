package com.yapp.crew.errors;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ZuulException {

	public UserNotFoundException(String message) {
		super(message, HttpStatus.FORBIDDEN.value(), "UserNotFoundException");
	}
}
