package com.yapp.crew.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.errors.AddressNotFoundException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.CategoryNotFoundException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.TagNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Address;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Board.BoardBuilder;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.Evaluation;
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
import com.yapp.crew.model.BoardListResponseInfo;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.producer.BoardProducer;
import java.util.List;
import java.util.Optional;
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
		BoardBuilder boardBuilder = Board.getBuilder();

		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));

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
	public List<BoardListResponseInfo> getBoardList(BoardFilterCondition boardFilterCondition) {
		// TODO: repository에서 DTO로 바로 리턴 하도록
		// TODO: user check 꼭 여기서 해야하는지
		User user = findUserById(boardFilterCondition.getUserId())
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		return filterBoard(boardFilterCondition)
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

	private List<Board> filterBoard(BoardFilterCondition boardFilterCondition) { return boardSearchAndFilterRepository.filter(boardFilterCondition); }

}
