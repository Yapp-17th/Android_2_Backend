package com.yapp.crew.filters;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.yapp.crew.auth.Auth;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.UserStatus;
import com.yapp.crew.errors.InactiveUserException;
import com.yapp.crew.errors.SuspendedUserException;
import com.yapp.crew.errors.TokenRequiredException;
import com.yapp.crew.errors.UserNotFoundException;
import com.yapp.crew.handlers.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j(topic = "Zuul Authentication Filter")
@Component
public class AuthenticationFilter extends ZuulFilter {

	private final Auth auth;
	private final UserRepository userRepository;
	private final ExceptionHandler exceptionHandler;

	@Autowired
	public AuthenticationFilter(Auth auth, UserRepository userRepository, ExceptionHandler exceptionHandler) {
		this.auth = auth;
		this.userRepository = userRepository;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public String filterType() {
		return PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 10;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		log.info("Method: {}, URL: {}", request.getMethod(), request.getRequestURL());

		if ((ctx.get("proxy") != null) && ctx.get("proxy").equals("base-service")) {
			return false;
		}
		if ((ctx.get("proxy") != null) && ctx.get("proxy").equals("login-service")) {
			return false;
		}
		return true;
	}

	@Override
	public Object run() {
		try {
			RequestContext ctx = RequestContext.getCurrentContext();
			HttpServletRequest request = ctx.getRequest();

			String token = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (token == null) {
				throw new TokenRequiredException("[Zuul Proxy Exception] Token is required but wasn't sent");
			}

			auth.verifyToken(token);
			Long userId = auth.parseUserIdFromToken(token);
			User user = userRepository.findUserById(userId)
					.orElseThrow(() -> new UserNotFoundException("[Zuul Proxy Exception] Cannot find user with id: " + userId));

			checkUserStatus(user.getStatus(), userId);

			ctx.addZuulRequestHeader("userId", userId.toString());
		} catch (Exception ex) {
			exceptionHandler.handleException(ex);
		}
		return null;
	}

	private void checkUserStatus(UserStatus userStatus, Long userId) throws InactiveUserException, SuspendedUserException {
		if (userStatus == UserStatus.INACTIVE) {
			throw new InactiveUserException("[Zuul Proxy Exception] Inactive user has accessed with id: " + userId);
		} else if (userStatus == UserStatus.SUSPENDED || userStatus == UserStatus.FORBIDDEN) {
			throw new SuspendedUserException("[Zuul Proxy Exception] Suspended user has accessed with id: " + userId);
		}
	}
}
