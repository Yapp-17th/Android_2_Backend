package com.yapp.crew.errors;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;

public class TokenRequiredException extends ZuulException {

	public TokenRequiredException(String message) {
		super(message, HttpStatus.FORBIDDEN.value(), "TokenRequiredException");
	}
}
