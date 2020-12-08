package com.yapp.crew.service;

import com.yapp.crew.config.JwtUtils;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.network.model.SimpleResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawService {

	private UserRepository userRepository;
	private JwtUtils jwtUtils;

	@Autowired
	public WithdrawService(UserRepository userRepository, JwtUtils jwtUtils) {
		this.userRepository = userRepository;
		this.jwtUtils = jwtUtils;
	}

	@Transactional
	public UserAuthResponse withdraw(String token) {
		long userId = Long.parseLong(jwtUtils.getUserIdFromToken(token));

		User user = getUserByUserId(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		updateUserInActive(user);

		return new UserAuthResponse(SimpleResponse.pass(ResponseType.WITHDRAW_SUCCESS));
	}

	private void updateUserInActive(User user) {
		user.setUserStatusInActive();
		userRepository.save(user);
	}

	private Optional<User> getUserByUserId(long userId) {
		return userRepository.findUserById(userId);
	}
}
