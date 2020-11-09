package com.yapp.crew.service;

import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.model.UserProfileInfo;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MyProfileService {

	private UserRepository userRepository;
	private EvaluationRepository evaluationRepository;

	@Autowired
	public MyProfileService(UserRepository userRepository, EvaluationRepository evaluationRepository) {
		this.userRepository = userRepository;
		this.evaluationRepository = evaluationRepository;
	}

	@Transactional
	public UserProfileInfo getProfile(long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));
		List<Evaluation> evaluations = findAllByUserId(userId);

		return UserProfileInfo.build(user, true, evaluations);
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}

	private List<Evaluation> findAllByUserId(Long userId) {
		return evaluationRepository.findAllByUserId(userId);
	}
}
