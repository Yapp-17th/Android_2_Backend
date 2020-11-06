package com.yapp.crew.service;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignInService {

	private UserRepository userRepository;
	private TokenService tokenService;

	@Autowired
	public SignInService(UserRepository userRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	public UserAuthResponse signIn(LoginUserInfo loginUserInfo) {
		User existingUser = getUserByOauthId(loginUserInfo.getOauthId())
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		if (existingUser.getStatus() == UserStatus.SUSPENDED) {
			log.info("suspended user가 로그인을 시도했습니다.");
			throw new SuspendedUserException("suspend user exception");
		} else if (existingUser.getStatus() == UserStatus.INACTIVE) {
			log.info("inactive user가 로그인을 시도했습니다.");
			throw new InactiveUserException("inactive user exception");
		}

		HttpHeaders httpHeaders = tokenService.setToken(existingUser);
		SimpleResponse simpleResponse = SimpleResponse.pass(ResponseType.SUCCESS);

		return new UserAuthResponse(httpHeaders, simpleResponse);
	}

	private Optional<User> getUserByOauthId(String oauthId) {
		log.info("user 가져오기 성공");
		return userRepository.findByOauthId(oauthId);
	}
}
