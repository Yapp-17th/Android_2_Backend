package com.yapp.crew.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.errors.AddressNotFoundException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.BoardTimeInvalidException;
import com.yapp.crew.domain.errors.CategoryNotFoundException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.TagNotFoundException;
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

	@Autowired
	public BoardService(
			BoardProducer boardProducer,
			TagRepository tagRepository,
			UserRepository userRepository,
			BoardRepository boardRepository,
			AddressRepository addressRepository,
			CategoryRepository categoryRepository,
			EvaluationRepository evaluationRepository
	) {
		this.boardProducer = boardProducer;
		this.tagRepository = tagRepository;
		this.userRepository = userRepository;
		this.boardRepository = boardRepository;
		this.addressRepository = addressRepository;
		this.categoryRepository = categoryRepository;
		this.evaluationRepository = evaluationRepository;
	}

	@Transactional
	public SimpleResponse postBoard(BoardPostRequiredInfo boardPostRequiredInfo, Long userId) {
		BoardBuilder boardBuilder = Board.getBuilder();

		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(ResponseType.USER_NOT_FOUND.getMessage()));

		Category category = findCategoryById(boardPostRequiredInfo.getCategory())
				.orElseThrow(() -> new CategoryNotFoundException("category not found"));

		Address address = findAddressById(boardPostRequiredInfo.getCity())
				.orElseThrow(() -> new AddressNotFoundException("address not found"));

		Tag tag = findTagById(boardPostRequiredInfo.getUserTag())
				.orElseThrow(() -> new TagNotFoundException("tag not found"));

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
	public List<BoardListResponseInfo> getBoardList(BoardFilter boardFilter) {
		List<Category> categories;
		List<Address> addresses;

		User user = findUserById(boardFilter.getUserId())
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		if (boardFilter.getCategory() != null) {
			categories = findAllCategory().stream()
					.filter(category -> boardFilter.getCategory().contains(category.getId()))
					.collect(Collectors.toList());
		}
		else {
			categories = findAllCategory();
		}

		if (boardFilter.getCity() != null) {
			addresses = findAllAddress().stream()
					.filter(address -> boardFilter.getCity().contains(address.getId()))
					.collect(Collectors.toList());
		}
		else {
			addresses = findAllAddress();
		}

		return sortBoardList(boardFilter.getSorting(), addresses, categories, user)
				.stream()
				.map(board -> BoardListResponseInfo.build(board, board.getUser()))
				.collect(Collectors.toList());
	}

	@Transactional
	public BoardContentResponseInfo getBoardContent(Long boardId, Long userId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		List<Evaluation> evaluations = findAllByEvaluatedId(userId);
		return BoardContentResponseInfo.build(board, evaluations);
	}

	@Transactional
	public SimpleResponse deleteBoard(Long boardId, Long userId) throws JsonProcessingException {
		Board board = findBoardById(boardId)
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
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		Category category = findCategoryById(boardPostRequiredInfo.getCategory())
				.orElseThrow(() -> new CategoryNotFoundException("category not found"));

		Address address = findAddressById(boardPostRequiredInfo.getCity())
				.orElseThrow(() -> new AddressNotFoundException("address not found"));

		Tag tag = findTagById(boardPostRequiredInfo.getUserTag())
				.orElseThrow(() -> new TagNotFoundException("tag not found"));

		List<Evaluation> evaluations = findAllByEvaluatedId(board.getUser().getId());

		board.updateBoard(
				boardPostRequiredInfo.getTitle(),
				boardPostRequiredInfo.getContent(),
				boardPostRequiredInfo.getPlace(),
				boardPostRequiredInfo.getRecruitNumber(),
				category,
				address,
				tag,
				boardPostRequiredInfo.getDate()
		);
		saveBoard(board);

		return BoardContentResponseInfo.build(board, evaluations);
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

	private List<Category> findAllCategory() {
		return categoryRepository.findAll();
	}

	private List<Address> findAllAddress() {
		return addressRepository.findAll();
	}

	private List<Board> sortBoardList(SortingType sorting, List<Address> addresses, List<Category> categories, User user) {
		List<Board> boards = filterBoardList(addresses, categories, user);

		if (sorting == SortingType.REMAIN) {
			return boards.stream()
					.sorted(Comparator.comparing(Board::getRemainRecruitNumber, Comparator.reverseOrder()))
					.collect(Collectors.toList());
		}
		else if (sorting == SortingType.DEADLINE) {
			return boards.stream()
					.sorted(Comparator.comparing(Board::getStartsAt, Comparator.naturalOrder()))
					.collect(Collectors.toList());
		}
		return boards.stream()
				.sorted(Comparator.comparing(BaseEntity::getCreatedAt, Comparator.reverseOrder()))
				.collect(Collectors.toList());
	}

	private List<Board> filterBoardList(List<Address> addresses, List<Category> categories, User user) {
		return findAllBoards(user).stream()
				.filter(board -> addresses.contains(board.getAddress()))
				.filter(board -> categories.contains(board.getCategory()))
				.collect(Collectors.toList());
	}

	private List<Board> findAllBoards(User user) {
		Set<Board> hiddenBoards = user.getUserHiddenBoard().stream()
				.map(HiddenBoard::getBoard).collect(Collectors.toSet());

		return boardRepository.findAll().stream()
				.filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode())
				.filter(board -> !hiddenBoards.contains(board))
				.collect(Collectors.toList());
	}
}