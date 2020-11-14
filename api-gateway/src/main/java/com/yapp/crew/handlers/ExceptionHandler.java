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

		if (ex instanceof InactiveUserException) {
			SimpleResponse response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INACTIVE_USER_FAIL);
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}

		if (ex instanceof SuspendedUserException) {
			SimpleResponse response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.SUSPENDED_USER_FAIL);
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}

		if (ex instanceof UserNotFoundException) {
			SimpleResponse response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.USER_NOT_FOUND);
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}

		if (ex instanceof TokenRequiredException) {
			SimpleResponse response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.TOKEN_REQUIRED);
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}

		if (ex instanceof WrongTokenPrefixException) {
			SimpleResponse response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.WRONG_TOKEN_PREFIX);
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}

		if (ex instanceof ExpiredJwtException) {
			SimpleResponse response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.EXPIRED_TOKEN);
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}

		if (ex instanceof MalformedJwtException) {
			SimpleResponse response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INVALID_JWT_SIGNATURE);
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}

		if (ex instanceof SignatureException) {
			SimpleResponse response = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INVALID_JWT_SIGNATURE);
			Gson gson = new Gson();
			String responseBody = gson.toJson(response);
			ctx.setResponseBody(responseBody);
		}
	}
}
