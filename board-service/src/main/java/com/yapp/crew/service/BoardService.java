package com.yapp.crew.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.errors.AddressNotFoundException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.BoardTimeInvalidException;
import com.yapp.crew.domain.errors.CategoryNotFoundException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.TagNotFoundException;
import com.yapp.crew.domain.errors.UnAuthorizedEventException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Address;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Board.BoardBuilder;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.Evaluation.EvaluationBuilder;
import com.yapp.crew.domain.model.Tag;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.AddressRepository;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.BoardSearchAndFilterRepository;
import com.yapp.crew.domain.repository.CategoryRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.TagRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.BoardCancel;
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.producer.BoardProducer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

	private final BoardProducer boardProducer;

	private final TagRepository tagRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final AddressRepository addressRepository;
	private final CategoryRepository categoryRepository;
	private final EvaluationRepository evaluationRepository;
	private final BoardSearchAndFilterRepository boardSearchAndFilterRepository;

	@Autowired
	public BoardService(
			BoardProducer boardProducer,
			TagRepository tagRepository,
			UserRepository userRepository,
			BoardRepository boardRepository,
			AddressRepository addressRepository,
			CategoryRepository categoryRepository,
			EvaluationRepository evaluationRepository,
			BoardSearchAndFilterRepository boardSearchAndFilterRepository
	) {
		this.boardProducer = boardProducer;
		this.tagRepository = tagRepository;
		this.userRepository = userRepository;
		this.boardRepository = boardRepository;
		this.addressRepository = addressRepository;
		this.categoryRepository = categoryRepository;
		this.evaluationRepository = evaluationRepository;
		this.boardSearchAndFilterRepository = boardSearchAndFilterRepository;
	}

	@Transactional
	public SimpleResponse postBoard(BoardPostRequiredInfo boardPostRequiredInfo, Long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		Category category = findCategoryById(boardPostRequiredInfo.getCategory())
				.orElseThrow(() -> new CategoryNotFoundException(boardPostRequiredInfo.getCategory()));

		Address address = findAddressById(boardPostRequiredInfo.getCity())
				.orElseThrow(() -> new AddressNotFoundException(boardPostRequiredInfo.getCity()));

		Tag tag = findTagById(boardPostRequiredInfo.getUserTag())
				.orElseThrow(() -> new TagNotFoundException(boardPostRequiredInfo.getUserTag()));

		BoardBuilder boardBuilder = Board.getBuilder();
		Board board = boardBuilder
				.withUser(user)
				.withCategory(category)
				.withAddress(address)
				.withStartsAt(boardPostRequiredInfo.getDate())
				.withTitle(boardPostRequiredInfo.getTitle())
				.withTag(tag)
				.withContent(boardPostRequiredInfo.getContent())
				.withPlace(boardPostRequiredInfo.getPlace())
				.withRecruitCount(boardPostRequiredInfo.getRecruitNumber())
				.build();

		saveBoard(board);

		return SimpleResponse.pass(ResponseType.BOARD_POST_SUCCESS);
	}

	@Transactional
	public List<BoardListResponseInfo> getBoardList(BoardFilterCondition boardFilterCondition, Pageable pageable) {
		User user = findUserById(boardFilterCondition.getUserId())
				.orElseThrow(() -> new UserNotFoundException(boardFilterCondition.getUserId()));

		return filterBoard(boardFilterCondition, pageable).stream()
				.map(board -> BoardListResponseInfo.build(board, user))
				.collect(Collectors.toList());
	}

	@Transactional
	public BoardContentResponseInfo getBoardContent(Long boardId, Long userId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		List<Evaluation> evaluations = findAllByEvaluatedId(userId);
		return BoardContentResponseInfo.build(board, userId, evaluations);
	}

	@Transactional
	public SimpleResponse deleteBoard(Long boardId, Long userId) throws JsonProcessingException {
		Board board = findMyBoardById(boardId, userId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		if (!board.getUser().getId().equals(userId)) {
			throw new InvalidRequestBodyException("Board Service - Incorrect board host with id: " + userId);
		}
		deleteBoard(board);
		boardProducer.produceBoardCanceledEvent(BoardCancel.build(boardId, userId));

		return SimpleResponse.pass(ResponseType.BOARD_DELETE_SUCCESS);
	}

	@Transactional
	public BoardContentResponseInfo editBoardContent(Long boardId, Long userId, BoardPostRequiredInfo boardPostRequiredInfo) {
		Board board = findMyBoardById(boardId, userId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		if (!board.getUser().getId().equals(userId)) {
			throw new UnAuthorizedEventException("Board Service - Incorrect board host with id: " + userId);
		}

		Category category = findCategoryById(boardPostRequiredInfo.getCategory())
				.orElseThrow(() -> new CategoryNotFoundException(boardPostRequiredInfo.getCategory()));

		Address address = findAddressById(boardPostRequiredInfo.getCity())
				.orElseThrow(() -> new AddressNotFoundException(boardPostRequiredInfo.getCity()));

		Tag tag = findTagById(boardPostRequiredInfo.getUserTag())
				.orElseThrow(() -> new TagNotFoundException(boardPostRequiredInfo.getUserTag()));

		List<Evaluation> evaluations = findAllByEvaluatedId(board.getUser().getId());

		BoardBuilder boardBuilder = Board.getBuilder();
		Board updateBoard = boardBuilder
				.withUser(board.getUser())
				.withTitle(boardPostRequiredInfo.getTitle())
				.withContent(boardPostRequiredInfo.getContent())
				.withPlace(boardPostRequiredInfo.getPlace())
				.withRecruitCount(boardPostRequiredInfo.getRecruitNumber())
				.withCategory(category)
				.withAddress(address)
				.withTag(tag)
				.withStartsAt(boardPostRequiredInfo.getDate())
				.build(board);
		saveBoard(updateBoard);

		return BoardContentResponseInfo.build(updateBoard, userId, evaluations);
	}

	private void deleteBoard(Board board) {
		saveEvaluationListAll(board);
		board.deleteBoard();
		boardRepository.save(board);
	}

	private void saveBoard(Board board) {
		if (board.getStartsAt().isBefore(LocalDateTime.now())) {
			throw new BoardTimeInvalidException("Board Service - Cannot set starting time before than current time");
		}
		boardRepository.save(board);
	}

	private List<Evaluation> findAllByEvaluatedId(Long userId) {
		return evaluationRepository.findAllByEvaluatedId(userId);
	}

	private Optional<Board> findMyBoardById(Long boardId, Long userId) {
		return boardRepository.findBoardById(boardId)
				.filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode())
				.filter(board -> board.getUser().getId().equals(userId));
	}

	private Optional<Board> findBoardById(Long boardId) {
		return boardRepository.findBoardById(boardId)
				.filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode());
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}

	private Optional<Category> findCategoryById(Long categoryId) {
		return categoryRepository.findCategoryById(categoryId);
	}

	private Optional<Address> findAddressById(Long addressId) {
		return addressRepository.findAddressById(addressId);
	}

	private Optional<Tag> findTagById(Long tagId) {
		return tagRepository.findTagById(tagId);
	}

	private List<Board> filterBoard(BoardFilterCondition boardFilterCondition, Pageable pageable) {
		return boardSearchAndFilterRepository.filter(boardFilterCondition, pageable);
	}

	private void saveEvaluationListAll(Board board) {
		EvaluationBuilder evaluationBuilder = Evaluation.getBuilder();

		List<Long> userIds = board.getAppliedUsers().stream()
				.filter(appliedUser -> appliedUser.getStatus() == AppliedStatus.APPROVED)
				.map(appliedUser -> appliedUser.getUser().getId())
				.collect(Collectors.toList());

		for (int i = 0; i < userIds.size(); i++) {
			for (int j = 0; j < userIds.size(); j++) {
				if (i == j) {
					continue;
				}

				Evaluation evaluation = evaluationBuilder
						.withBoard(board)
						.withEvaluateId(userIds.get(i))
						.withEvaluateId(userIds.get(j))
						.withIsDislike(false)
						.withIsLike(false)
						.build();
				evaluationRepository.save(evaluation);
			}
		}
	}
}
