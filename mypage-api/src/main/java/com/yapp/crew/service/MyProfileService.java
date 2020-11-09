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
import com.yapp.crew.model.HistoryListInfo;
import com.yapp.crew.model.UserProfileInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MyProfileService {

	private UserRepository userRepository;
	private EvaluationRepository evaluationRepository;
	private BoardRepository boardRepository;

	@Autowired
	public MyProfileService(UserRepository userRepository, EvaluationRepository evaluationRepository, BoardRepository boardRepository) {
		this.userRepository = userRepository;
		this.evaluationRepository = evaluationRepository;
		this.boardRepository = boardRepository;
	}

	@Transactional
	public UserProfileInfo getProfile(long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));
		List<Evaluation> evaluations = findAllByEvaluatedId(userId);

		return UserProfileInfo.build(user, true, evaluations);
	}

	@Transactional
	public List<HistoryListInfo> getHistoryList(long userId, String type) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		if (StringUtils.equalsIgnoreCase(type, "continue")) {
			List<Board> boards = findAllBoards(user).stream()
					.filter(board -> board.getStatus() == BoardStatus.RECRUITING || board.getStatus() == BoardStatus.COMPLETE)
					.collect(Collectors.toList());

			return boards.stream()
					.map(board -> HistoryListInfo.build(board, user))
					.collect(Collectors.toList());
		}

		List<Board> boards = findAllBoards(user).stream()
				.filter(board -> board.getStatus() == BoardStatus.CANCELED || board.getStatus() == BoardStatus.FINISHED)
				.collect(Collectors.toList());

		return boards.stream()
				.map(board -> HistoryListInfo.build(board, user))
				.collect(Collectors.toList());
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}

	private List<Evaluation> findAllByEvaluatedId(Long userId) {
		return evaluationRepository.findAllByEvaluatedId(userId);
	}

	private List<Board> findAllBoards(User user) {
		return boardRepository.findAll().stream()
				.filter(board -> board.getUser().equals(user) ||
						(board.getAppliedUsers().stream()
								.map(appliedUser -> appliedUser.getUser().equals(user) && appliedUser.getStatus() == AppliedStatus.APPROVED).count() != 0))
				.collect(Collectors.toList());
	}
}
