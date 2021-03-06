package com.yapp.crew.service;

import com.yapp.crew.config.JwtUtils;
import com.yapp.crew.domain.errors.ForbiddenUserSignUpException;
import com.yapp.crew.domain.errors.InactiveUserException;
import com.yapp.crew.domain.errors.SuspendedUserException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.UserStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.LoginUserInfo;
import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.network.model.SimpleResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class SignInService {

	private UserRepository userRepository;
	private TokenService tokenService;
	private JwtUtils jwtUtils;

	@Autowired
	public SignInService(UserRepository userRepository, TokenService tokenService, JwtUtils jwtUtils) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
		this.jwtUtils = jwtUtils;
	}

	public UserAuthResponse signIn(LoginUserInfo loginUserInfo) {
		User existingUser = getUserByOauthId(loginUserInfo.getOauthId())
				.orElseThrow(() -> new UserNotFoundException(Long.parseLong(loginUserInfo.getOauthId())));

		if (existingUser.getStatus() == UserStatus.SUSPENDED) {
			throw new SuspendedUserException(existingUser.getId());
		} else if (existingUser.getStatus() == UserStatus.INACTIVE) {
			throw new InactiveUserException(existingUser.getId());
		} else if (existingUser.getStatus() == UserStatus.FORBIDDEN) {
			throw new ForbiddenUserSignUpException(existingUser.getId());
		}

		HttpHeaders httpHeaders = tokenService.setToken(existingUser);
		SimpleResponse simpleResponse = SimpleResponse.pass(ResponseType.SUCCESS);

		return new UserAuthResponse(httpHeaders, simpleResponse);
	}

	public UserAuthResponse autoSignIn(String token) {
		long userId = jwtUtils.getUserIdFromToken(token);

		User user = getUserByUserId(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		HttpHeaders httpHeaders = tokenService.refreshToken(user, token);
		SimpleResponse simpleResponse = SimpleResponse.pass(ResponseType.SUCCESS);

		return new UserAuthResponse(httpHeaders, simpleResponse);
	}

	private Optional<User> getUserByOauthId(String oauthId) {
		return userRepository.findByOauthId(oauthId);
	}

	private Optional<User> getUserByUserId(long userId) {
		return userRepository.findUserById(userId);
	}
}
