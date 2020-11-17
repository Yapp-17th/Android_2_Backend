package com.yapp.crew.service;

import com.yapp.crew.domain.errors.AddressNotFoundException;
import com.yapp.crew.domain.errors.CategoryNotFoundException;
import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.domain.errors.UserDuplicateFieldException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Address;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.domain.model.UserExercise;
import com.yapp.crew.domain.model.UserExercise.UserExerciseBuilder;
import com.yapp.crew.domain.repository.AddressRepository;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.CategoryRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.UserExerciseRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.domain.type.UniqueIndexEnum;
import com.yapp.crew.model.HistoryListInfo;
import com.yapp.crew.model.UserProfileInfo;
import com.yapp.crew.model.UserUpdateRequest;
import com.yapp.crew.network.model.SimpleResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MyProfileService {

	private UserRepository userRepository;
	private EvaluationRepository evaluationRepository;
	private BoardRepository boardRepository;
	private AddressRepository addressRepository;
	private CategoryRepository categoryRepository;
	private UserExerciseRepository userExerciseRepository;

	@Autowired
	public MyProfileService(UserRepository userRepository, EvaluationRepository evaluationRepository, BoardRepository boardRepository, AddressRepository addressRepository, CategoryRepository categoryRepository, UserExerciseRepository userExerciseRepository) {
		this.userRepository = userRepository;
		this.evaluationRepository = evaluationRepository;
		this.boardRepository = boardRepository;
		this.addressRepository = addressRepository;
		this.categoryRepository = categoryRepository;
		this.userExerciseRepository = userExerciseRepository;
	}

	@Transactional
	public UserProfileInfo getProfile(long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));
		List<Evaluation> evaluations = findAllByEvaluatedId(userId);

		return UserProfileInfo.build(user, true, evaluations);
	}

	@Transactional
	public SimpleResponse updateProfile(long userId, UserUpdateRequest userUpdateRequest) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));
		updateUser(user, userUpdateRequest);

		return SimpleResponse.pass(ResponseType.USERINFO_UPDATE_SUCCESS);
	}

	@Transactional
	public List<HistoryListInfo> getHistoryList(long userId, String type) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		if (StringUtils.equalsIgnoreCase(type, "continue")) {
			return findAllBoards(user).stream()
					.filter(board -> board.getStatus() == BoardStatus.RECRUITING || board.getStatus() == BoardStatus.COMPLETE)
					.map(board -> HistoryListInfo.build(board, user))
					.collect(Collectors.toList());
		}

		return findAllBoards(user).stream()
				.filter(board -> board.getStatus() == BoardStatus.CANCELED || board.getStatus() == BoardStatus.FINISHED)
				.map(board -> HistoryListInfo.build(board, user))
				.collect(Collectors.toList());
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}

	private Optional<Address> findAddressById(Long addressId) {
		return addressRepository.findAddressById(addressId);
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

	private void updateUser(User user, UserUpdateRequest userUpdateRequest) {
		Address address = findAddressById(userUpdateRequest.getAddress())
				.orElseThrow(() -> new AddressNotFoundException("address not found"));

		UserBuilder userBuilder = User.getBuilder();
		User updatedUser = userBuilder
				.withEmail(userUpdateRequest.getEmail())
				.withNickname(userUpdateRequest.getNickName())
				.withUsername(userUpdateRequest.getUserName())
				.withIntro(userUpdateRequest.getIntro())
				.withAddress(address)
				.build(user);

		try {
			userRepository.save(updatedUser);
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				log.info("Request server error: " + e.getLocalizedMessage());
				for (UniqueIndexEnum uniqueIndexEnum : UniqueIndexEnum.values()) {
					if (StringUtils.containsIgnoreCase(e.getLocalizedMessage(), uniqueIndexEnum.toString())) {
						throw new UserDuplicateFieldException(uniqueIndexEnum.getName());
					}
				}
			}
			throw new InternalServerErrorException("internal server error");
		}

		removeAllUserExercise(updatedUser);
		for (long categoryId : userUpdateRequest.getCategory()) {
			Category userCategory = findCategoryById(categoryId)
					.orElseThrow(() -> new CategoryNotFoundException("category not found"));

			saveUserExercise(updatedUser, userCategory);
		}
	}

	private Optional<Category> findCategoryById(Long id) {
		return categoryRepository.findCategoryById(id);
	}

	private void removeAllUserExercise(User user) {
		List<UserExercise> userExercises = userExerciseRepository.findAllByUserId(user.getId());
		for (UserExercise userExercise : userExercises) {
			userExerciseRepository.delete(userExercise);
		}
	}

	private void saveUserExercise(User user, Category category) {
		UserExerciseBuilder userExerciseBuilder = UserExercise.getBuilder();
		UserExercise userExercise = userExerciseBuilder
				.withUser(user)
				.withCategory(category)
				.build();

		userExerciseRepository.save(userExercise);
	}
}
