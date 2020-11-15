package com.yapp.crew.errors;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;

public class WrongTokenPrefixException extends ZuulException {

	public WrongTokenPrefixException(String message) {
		super(message, HttpStatus.FORBIDDEN.value(), "WrongTokenPrefixException");
	}
}
