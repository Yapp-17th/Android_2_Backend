package com.yapp.crew.service;

import com.yapp.crew.domain.errors.AddressNotFoundException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
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
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.domain.type.SortingType;
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

	private BoardRepository boardRepository;
	private UserRepository userRepository;
	private AddressRepository addressRepository;
	private CategoryRepository categoryRepository;
	private TagRepository tagRepository;
	private EvaluationRepository evaluationRepository;

	@Autowired
	public BoardService(BoardRepository boardRepository, UserRepository userRepository, AddressRepository addressRepository, CategoryRepository categoryRepository, TagRepository tagRepository, EvaluationRepository evaluationRepository) {
		this.boardRepository = boardRepository;
		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
		this.categoryRepository = categoryRepository;
		this.tagRepository = tagRepository;
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
		} else {
			categories = findAllCategory();
		}

		if (boardFilter.getCity() != null) {
			addresses = findAllAddress().stream()
					.filter(address -> boardFilter.getCity().contains(address.getId()))
					.collect(Collectors.toList());
		} else {
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

		List<Evaluation> evaluations = findAllByUserId(userId);
		return BoardContentResponseInfo.build(board, evaluations);
	}

	@Transactional
	public SimpleResponse deleteBoard(Long boardId, Long userId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		if (!board.getUser().getId().equals(userId)) {
			throw new InvalidRequestBodyException("invalid user_id");
		}

		deleteBoard(board);
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
		List<Evaluation> evaluations = findAllByUserId(board.getUser().getId());

		board.updateBoard(boardPostRequiredInfo.getTitle(), boardPostRequiredInfo.getContent(), boardPostRequiredInfo.getPlace(), boardPostRequiredInfo.getRecruitNumber(), category, address, tag, boardPostRequiredInfo.getDate());
		saveBoard(board);

		return BoardContentResponseInfo.build(board, evaluations);
	}

	private void deleteBoard(Board board) {
		board.deleteBoard();
		boardRepository.save(board);
	}

	private void saveBoard(Board board) {
		boardRepository.save(board);
	}

	private List<Evaluation> findAllByUserId(Long userId) {
		return evaluationRepository.findAllByUserId(userId);
	}

	private Optional<Board> findBoardById(Long boardId) {
		return boardRepository.findBoardById(boardId).filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode());
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
		} else if (sorting == SortingType.DEADLINE) {
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
		Set<Board> hiddenBoards = user.getUserHiddenBoard().stream().map(HiddenBoard::getBoard).collect(Collectors.toSet());

		return boardRepository.findAll().stream()
				.filter(board -> board.getStatus().getCode() != BoardStatus.CANCELED.getCode())
				.filter(board -> !hiddenBoards.contains(board))
				.collect(Collectors.toList());
	}
}
