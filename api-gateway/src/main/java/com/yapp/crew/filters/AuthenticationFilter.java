package com.yapp.crew.filters;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

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
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		try {
			RequestContext ctx = RequestContext.getCurrentContext();
			HttpServletRequest request = ctx.getRequest();

			String token = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (token == null) {
				throw new TokenRequiredException("[Zuul Proxy Exception] token is required");
			}

			auth.verifyToken(token);
			Long userId = auth.parseUserIdFromToken(token);
			User user = userRepository.findUserById(userId)
					.orElseThrow(() -> new UserNotFoundException("[Zuul Proxy Exception] user not found"));

			checkUserStatus(user.getStatus());

			ctx.addZuulRequestHeader("userId", userId.toString());
		}
		catch (Exception ex) {
			exceptionHandler.handleException(ex);
		}
		return null;
	}

	private void checkUserStatus(UserStatus userStatus) throws InactiveUserException, SuspendedUserException {
		if (userStatus.equals(UserStatus.INACTIVE)) {
			throw new InactiveUserException("[Zuul Proxy Exception] Inactive user");
		}
		else if (userStatus.equals(UserStatus.SUSPENDED)) {
			throw new SuspendedUserException("[Zuul Proxy Exception] suspended user");
		}
	}
}
