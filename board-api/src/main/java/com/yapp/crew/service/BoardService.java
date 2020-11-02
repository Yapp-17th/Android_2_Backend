package com.yapp.crew.service;

import com.yapp.crew.domain.model.Address;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.Board.BoardBuilder;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.Tag;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.AddressRepository;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.CategoryRepository;
import com.yapp.crew.domain.repository.EvaluationRepository;
import com.yapp.crew.domain.repository.TagRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.type.ExerciseType;
import com.yapp.crew.exception.InternalServerErrorException;
import com.yapp.crew.model.BoardContentResponseInfo;
import com.yapp.crew.model.BoardPostRequiredInfo;
import com.yapp.crew.model.SimpleResponse;
import com.yapp.crew.utils.ResponseMessage;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public SimpleResponse postBoard(BoardPostRequiredInfo boardPostRequiredInfo, Long userId) {
    BoardBuilder boardBuilder = Board.getBuilder();

    User user = findUserById(userId)
        .orElseThrow(InternalServerErrorException::new);
    Category category = findCategoryById(boardPostRequiredInfo.getCategory())
        .orElseThrow(InternalServerErrorException::new);
    Address address = findAddressById(boardPostRequiredInfo.getCity())
        .orElseThrow(InternalServerErrorException::new);
    Tag tag = findTagById(boardPostRequiredInfo.getUserTag())
        .orElseThrow(InternalServerErrorException::new);

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

  public BoardContentResponseInfo getBoardContent(Long boardId, Long userId) {
    Board board = findBoardById(boardId)
        .orElseThrow(InternalServerErrorException::new);
    List<Evaluation> evaluations = findAllByUserId(userId);

    if (evaluations == null) {
      throw new InternalServerErrorException();
    }

    return BoardContentResponseInfo.build(board, evaluations);
  }

  public SimpleResponse deleteBoard(Long boardId, Long userId) {
    Board board = findBoardById(boardId)
        .orElseThrow(InternalServerErrorException::new);

    if (!board.getUser().getId().equals(userId)) {
      SimpleResponse.fail(ResponseMessage.BOARD_DIFF_USERID.getMessage());
    }
    // TODO: try - catch: internal server error
    deleteBoard(board);
    return SimpleResponse.pass(ResponseMessage.BOARD_DELETE_SUCCESS.getMessage());
  }

  public BoardContentResponseInfo editBoardContent(Long boardId, Long userId, BoardPostRequiredInfo boardPostRequiredInfo) {
    Board board = findBoardById(boardId)
        .orElseThrow(InternalServerErrorException::new);
    Category category = findCategoryById(boardPostRequiredInfo.getCategory())
        .orElseThrow(InternalServerErrorException::new);
    Address address = findAddressById(boardPostRequiredInfo.getCity())
        .orElseThrow(InternalServerErrorException::new);
    Tag tag = findTagById(boardPostRequiredInfo.getUserTag())
        .orElseThrow(InternalServerErrorException::new);
    List<Evaluation> evaluations = findAllByUserId(board.getUser().getId()); // TODO: null 체크

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
    log.info("evaluatino 가져오기 성공");
    return evaluationRepository.findAllByUserId(userId);
  }

  private Optional<Board> findBoardById(Long boardId) {
    log.info("board 가져오기 성공");
    return boardRepository.findBoardById(boardId);
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
}
