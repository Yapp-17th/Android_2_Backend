package com.yapp.crew.service;

import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserAuthenticationService {

	private UserRepository userRepository;

	@Autowired
	public UserAuthenticationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public void countUserSuspendedDays() {
		List<User> suspendedUser = userRepository.findSuspendedUsers();
		log.info("suspended users -> {}", suspendedUser);

		suspendedUser.forEach(user -> {
			if (user.getSuspendedDay() == 7) {
				user.setUserStatusActive();
				user.resetSuspendedDays();
				log.info("set user status active -> {}", user.getId());
			} else {
				user.increaseSuspendedDays();
			}
		});
		userRepository.saveAll(suspendedUser);
	}
}
