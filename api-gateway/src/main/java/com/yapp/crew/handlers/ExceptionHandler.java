package com.yapp.crew.handlers;

import com.google.gson.Gson;
import com.netflix.zuul.context.RequestContext;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.errors.InactiveUserException;
import com.yapp.crew.errors.SuspendedUserException;
import com.yapp.crew.errors.TokenRequiredException;
import com.yapp.crew.errors.UserNotFoundException;
import com.yapp.crew.errors.WrongTokenPrefixException;
import com.yapp.crew.network.model.SimpleResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler {

	public void handleException(Exception ex) {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(HttpStatus.OK.value());
		ctx.getResponse().setContentType("application/json;charset=UTF-8");

		SimpleResponse response = null;

		if (ex instanceof InactiveUserException) {
			response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INACTIVE_USER_FAIL);
		}

		else if (ex instanceof SuspendedUserException) {
			response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.SUSPENDED_USER_FAIL);
		}

		else if (ex instanceof UserNotFoundException) {
			response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.USER_NOT_FOUND);
		}

		else if (ex instanceof TokenRequiredException) {
			response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.TOKEN_REQUIRED);
		}

		else if (ex instanceof WrongTokenPrefixException) {
			response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.WRONG_TOKEN_PREFIX);
		}

		else if (ex instanceof ExpiredJwtException) {
			response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.EXPIRED_TOKEN);
		}

		else if (ex instanceof MalformedJwtException) {
			response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INVALID_TOKEN);
		}

		else if (ex instanceof SignatureException) {
			response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INVALID_JWT_SIGNATURE);
		}

		if (response != null) {
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}
	}
}
