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
import com.yapp.crew.domain.status.GroupStatus;
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardFilter;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.model.BoardResponseInfo;
import com.yapp.crew.model.SimpleResponse;
import com.yapp.crew.utils.ResponseMessage;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    return SimpleResponse.pass(ResponseMessage.BOARD_POST_SUCCESS.getMessage());
  }

  @Transactional
  public List<BoardResponseInfo> getBoardList(BoardFilter boardFilter) {
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

    List<Board> filteredBoard = filterBoardList(findAllBoards(user), addresses, categories, user);

    return sortBoardList(filteredBoard, boardFilter.getSorting())
        .stream()
        .map(board -> BoardResponseInfo.build(board, board.getUser()))
        .collect(Collectors.toList());
  }

  @Transactional
  public BoardContentResponseInfo getBoardContent(Long boardId, Long userId) {
    User user = findUserById(userId)
        .orElseThrow(() -> new UserNotFoundException("user not found"));
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
      throw new InvalidRequestBodyException(ResponseMessage.INVALID_REQUEST_BODY.getMessage());
    }

    deleteBoard(board);
    return SimpleResponse.pass(ResponseMessage.BOARD_DELETE_SUCCESS.getMessage());
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
    log.info("board 삭제 성공");
    board.deleteBoard();
    boardRepository.save(board);
  }

  private void saveBoard(Board board) {
    log.info("board 저장 성공");
    boardRepository.save(board);
  }

  private List<Evaluation> findAllByUserId(Long userId) {
    log.info("evaluation 가져오기 성공");
    return evaluationRepository.findAllByUserId(userId);
  }

  private Optional<Board> findBoardById(Long boardId) {
    log.info("board 가져오기 성공");
    return boardRepository.findBoardById(boardId).filter(board -> board.getStatus().getCode() < GroupStatus.CANCELED.getCode());
  }

  private Optional<User> findUserById(Long userId) {
    log.info("user 가져오기 성공");
    return userRepository.findUserById(userId);
  }

  private Optional<Category> findCategoryById(Long categoryId) {
    log.info("category 가져오기 성공");
    return categoryRepository.findCategoryById(categoryId);
  }

  private Optional<Address> findAddressById(Long addressId) {
    log.info("address 가져오기 성공");
    return addressRepository.findAddressById(addressId);
  }

  private Optional<Tag> findTagById(Long tagId) {
    log.info("tag 가져오기 성공");
    return tagRepository.findTagById(tagId);
  }

  private List<Board> findAllBoards(User user) {
    Set<Board> hiddenBoards = user.getUserHiddenBoard().stream().map(HiddenBoard::getBoard).collect(Collectors.toSet());

    log.info("모든 board 리스트 가져오기 성공");
    return boardRepository.findAll().stream()
        .filter(board -> board.getStatus().getCode() < GroupStatus.CANCELED.getCode())
        .filter(board -> !hiddenBoards.contains(board))
        .collect(Collectors.toList());
  }

  private List<Category> findAllCategory() {
    log.info("모든 category 리스트 가져오기 성공");
    return categoryRepository.findAll();
  }

  private List<Address> findAllAddress() {
    log.info("모든 address 리스트 가져오기 성공");
    return addressRepository.findAll();
  }

  private List<Board> filterBoardList(List<Board> boards, List<Address> addresses, List<Category> categories, User user) {
    Set<Board> hiddenBoards = user.getUserHiddenBoard().stream().map(HiddenBoard::getBoard).collect(Collectors.toSet());

    return boards.stream()
        .filter(board -> addresses.contains(board.getAddress()))
        .filter(board -> categories.contains(board.getCategory()))
        .filter(board -> !hiddenBoards.contains(board))
        .collect(Collectors.toList());
  }

  private List<Board> sortBoardList(List<Board> boards, String sorting) {
    if (StringUtils.equalsIgnoreCase(sorting, "remain")) {
      return boards.stream()
          .sorted(Comparator.comparing(Board::getRemainRecruitNumber, Comparator.reverseOrder()))
          .collect(Collectors.toList());
    } else if (StringUtils.equalsIgnoreCase(sorting, "deadline")) {
      return boards.stream()
          .sorted(Comparator.comparing(Board::getStartsAt, Comparator.naturalOrder()))
          .collect(Collectors.toList());
    }
    return boards.stream()
        .sorted(Comparator.comparing(BaseEntity::getCreatedAt, Comparator.reverseOrder()))
        .collect(Collectors.toList());
  }
}
