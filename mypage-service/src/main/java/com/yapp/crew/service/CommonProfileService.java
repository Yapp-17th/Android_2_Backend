package com.yapp.crew.service;

import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.status.UserStatus;
import com.yapp.crew.model.HistoryListInfo;
import com.yapp.crew.model.UserProfileInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonProfileService {

	private UserRepository userRepository;
	private EvaluationRepository evaluationRepository;
	private BoardRepository boardRepository;

	@Autowired
	public CommonProfileService(UserRepository userRepository, EvaluationRepository evaluationRepository, BoardRepository boardRepository) {
		this.userRepository = userRepository;
		this.evaluationRepository = evaluationRepository;
		this.boardRepository = boardRepository;
	}

	@Transactional
	public UserProfileInfo getProfile(long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		List<Evaluation> evaluations = findAllByEvaluatedId(userId);

		return UserProfileInfo.build(user, false, evaluations);
	}

	@Transactional
	public List<HistoryListInfo> getHistoryList(long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		return findAllBoards(user).stream()
				.filter(board -> board.getStatus() == BoardStatus.CANCELED || board.getStatus() == BoardStatus.FINISHED)
				.map(board -> HistoryListInfo.build(board, user))
				.collect(Collectors.toList());
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId).stream()
				.filter(user -> user.getStatus() == UserStatus.ACTIVE)
				.findFirst();
	}

	private List<Evaluation> findAllByEvaluatedId(Long userId) {
		return evaluationRepository.findAllByEvaluatedId(userId);
	}

	private List<Board> findAllBoards(User user) {
		return boardRepository.findAll().stream()
				.filter(board -> board.getUser().getId().equals(user.getId()) ||
						(board.getAppliedUsers().stream()
								.map(appliedUser -> appliedUser.getUser().getId().equals(user.getId())
										&& appliedUser.getStatus() == AppliedStatus.APPROVED).count() != 0))
				.collect(Collectors.toList());
	}
}
