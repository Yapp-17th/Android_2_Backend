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
import com.yapp.crew.domain.model.BaseEntity;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Board.BoardBuilder;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.HiddenBoard;
import com.yapp.crew.domain.model.Tag;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.AddressRepository;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.BoardSearchAndFilterRepository;
import com.yapp.crew.domain.repository.CategoryRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.TagRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.BoardCancel;
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardFilter;
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.producer.BoardProducer;
import com.yapp.crew.utils.SortingType;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
				.orElseThrow(() -> new UserNotFoundException(ResponseType.USER_NOT_FOUND.getMessage()));

		Category category = findCategoryById(boardPostRequiredInfo.getCategory())
				.orElseThrow(() -> new CategoryNotFoundException("category not found"));

		Address address = findAddressById(boardPostRequiredInfo.getCity())
				.orElseThrow(() -> new AddressNotFoundException("address not found"));

		Tag tag = findTagById(boardPostRequiredInfo.getUserTag())
				.orElseThrow(() -> new TagNotFoundException("tag not found"));

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
		return filterBoard(boardFilterCondition, pageable)
				.stream()
				.map(board -> BoardListResponseInfo.build(board, boardFilterCondition.getUserId()))
				.collect(Collectors.toList());
	}

	@Transactional
	public BoardContentResponseInfo getBoardContent(Long boardId, Long userId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		List<Evaluation> evaluations = findAllByEvaluatedId(userId);
		return BoardContentResponseInfo.build(board, userId, evaluations);
	}

	@Transactional
	public SimpleResponse deleteBoard(Long boardId, Long userId) throws JsonProcessingException {
		Board board = findMyBoardById(boardId, userId)
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		if (!board.getUser().getId().equals(userId)) {
			throw new InvalidRequestBodyException("invalid user_id");
		}
		deleteBoard(board);
		boardProducer.produceBoardCanceledEvent(BoardCancel.build(boardId, userId));

		return SimpleResponse.pass(ResponseType.BOARD_DELETE_SUCCESS);
	}

	@Transactional
	public BoardContentResponseInfo editBoardContent(Long boardId, Long userId, BoardPostRequiredInfo boardPostRequiredInfo) {
		Board board = findMyBoardById(boardId, userId)
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		if (!board.getUser().getId().equals(userId)) {
			throw new UnAuthorizedEventException("not authorized edit");
		}

		Category category = findCategoryById(boardPostRequiredInfo.getCategory())
				.orElseThrow(() -> new CategoryNotFoundException("category not found"));

		Address address = findAddressById(boardPostRequiredInfo.getCity())
				.orElseThrow(() -> new AddressNotFoundException("address not found"));

		Tag tag = findTagById(boardPostRequiredInfo.getUserTag())
				.orElseThrow(() -> new TagNotFoundException("tag not found"));

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
		board.deleteBoard();
		boardRepository.save(board);
	}

	private void saveBoard(Board board) {
		if (board.getStartsAt().isBefore(LocalDateTime.now())) {
			throw new BoardTimeInvalidException("board time invalid");
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
}
