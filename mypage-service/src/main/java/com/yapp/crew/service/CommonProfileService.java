package com.yapp.crew.service;

import com.yapp.crew.domain.condition.HistoryCondition;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.UserProfileHistoryRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.UserStatus;
import com.yapp.crew.model.HistoryListInfo;
import com.yapp.crew.model.UserProfileInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonProfileService {

	private UserRepository userRepository;
	private EvaluationRepository evaluationRepository;
	private UserProfileHistoryRepository userProfileHistoryRepository;

	@Autowired
	public CommonProfileService(UserRepository userRepository, EvaluationRepository evaluationRepository, UserProfileHistoryRepository userProfileHistoryRepository) {
		this.userRepository = userRepository;
		this.evaluationRepository = evaluationRepository;
		this.userProfileHistoryRepository = userProfileHistoryRepository;
	}

	@Transactional
	public UserProfileInfo getProfile(long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		List<Evaluation> evaluations = findAllByEvaluatedId(userId);

		return UserProfileInfo.build(user, false, evaluations);
	}

	@Transactional
	public List<HistoryListInfo> getHistoryList(HistoryCondition historyCondition, Pageable pageable) {
		User user = findUserById(historyCondition.getUserId())
				.orElseThrow(() -> new UserNotFoundException(historyCondition.getUserId()));

		return userProfileHistoryRepository.getHistory(historyCondition, pageable)
				.stream()
				.map(board -> HistoryListInfo.build(board, user))
				.collect(Collectors.toList());
	}

	private Optional<User> findUserById(long userId) {
		return userRepository.findUserById(userId).stream()
				.filter(User::isValidUser)
				.findFirst();
	}

	private List<Evaluation> findAllByEvaluatedId(long userId) {
		return evaluationRepository.findAllByEvaluatedId(userId);
	}

}
