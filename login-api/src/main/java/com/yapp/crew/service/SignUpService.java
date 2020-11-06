package com.yapp.crew.service;

import com.yapp.crew.domain.errors.CategoryNotFoundException;
import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Address;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.domain.model.UserExercise;
import com.yapp.crew.domain.model.UserExercise.UserExerciseBuilder;
import com.yapp.crew.domain.repository.AddressRepository;
import com.yapp.crew.domain.repository.CategoryRepository;
import com.yapp.crew.domain.repository.UserExerciseRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.status.UserStatus;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.model.SignupUserInfo;
import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.network.model.SimpleResponse;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignUpService {

  private UserRepository userRepository;
  private CategoryRepository categoryRepository;
  private AddressRepository addressRepository;
  private UserExerciseRepository userExerciseRepository;

  private TokenService tokenService;

  @Autowired
  public SignUpService(UserRepository userRepository, CategoryRepository categoryRepository, AddressRepository addressRepository, UserExerciseRepository userExerciseRepository, TokenService tokenService) {
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.addressRepository = addressRepository;
    this.userExerciseRepository = userExerciseRepository;
    this.tokenService = tokenService;
  }

  public UserAuthResponse signUp(SignupUserInfo signupUserInfo) {
    Optional<User> existingUser = findUserByOauthId(signupUserInfo.getOauthId());
    if (existingUser.isPresent()) {
      return signUpExistingUser(existingUser.get());
    }

    return signUpNewUser(signupUserInfo);
  }

  public UserAuthResponse signUpNewUser(SignupUserInfo signupUserInfo) {
    UserBuilder userBuilder = User.getBuilder();

    Address address = findAddressById(signupUserInfo.getAddress())
        .orElseThrow(InternalError::new);

    User user = userBuilder
        .withOauthId(signupUserInfo.getOauthId())
        .withAccessToken(signupUserInfo.getAccessToken())
        .withEmail(signupUserInfo.getEmail())
        .withNickname(signupUserInfo.getNickName())
        .withUsername(signupUserInfo.getUserName())
        .withAddress(address)
        .withIntro(signupUserInfo.getIntro())
        .build();

    try {
      save(user, signupUserInfo.getCategory());
    } catch (Exception e) {
      if (e.getCause() instanceof ConstraintViolationException) {
        // TODO: 어떤 필드가 duplicate인지 어떻게 뽑아내지?
        log.info("Request server error: " + e.getLocalizedMessage());
        throw new InvalidRequestBodyException(ResponseType.INVALID_REQUEST_BODY.getMessage());
      }

      log.info("Internal server error: " + e.getMessage());
      throw new InternalServerErrorException("internal server error");
    }

    try {
      HttpHeaders httpHeaders = tokenService.setToken(user);
      SimpleResponse simpleResponse = SimpleResponse.pass(ResponseType.SIGNUP_SUCCESS);

      return new UserAuthResponse(httpHeaders, simpleResponse);
    } catch (Exception e) {
      log.info("Internal server error: " + e.getMessage());
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  public UserAuthResponse signUpExistingUser(User user) {
    if (user.getStatus() == UserStatus.INACTIVE) {
      updateUserActive(user);
      HttpHeaders httpHeaders = null;

      try {
        httpHeaders = tokenService.setToken(user);
      } catch (Exception e) {
        throw new InternalServerErrorException("internal server error");
      }

      if (httpHeaders != null) {
        SimpleResponse simpleResponse = SimpleResponse.pass(ResponseType.SIGNUP_SUCCESS);
        return new UserAuthResponse(httpHeaders, simpleResponse);
      }
    }

    return new UserAuthResponse(SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INTERNAL_SERVER_FAIL));
  }

  private void saveUser(User user) {
    userRepository.save(user);
    log.info("user login 성공");
  }

  public void saveUserExercise(User user, Category category) {
    UserExerciseBuilder userExerciseBuilder = UserExercise.getBuilder();
    UserExercise userExercise = userExerciseBuilder
        .withUser(user)
        .withCategory(category)
        .build();

    userExerciseRepository.save(userExercise);
  }

  @Transactional
  public void save(User user, List<Long> category) {
    saveUser(user);
    User savedUser = findUserByOauthId(user.getOauthId())
        .orElseThrow(() -> new UserNotFoundException("user not found"));

    for (long categoryId : category) {
      Category userCategory = findCategoryById(categoryId)
          .orElseThrow(() -> new CategoryNotFoundException("category not found"));

      saveUserExercise(savedUser, userCategory);
    }
  }

  private Optional<Category> findCategoryById(Long id) {
    log.info("find category by category_id");
    return categoryRepository.findCategoryById(id);
  }

  private Optional<User> findUserByOauthId(String oauthId) {
    log.info("find user by oauth_id");
    return userRepository.findByOauthId(oauthId);
  }

  private Optional<Address> findAddressById(Long id) {
    log.info("find address by address_id");
    return addressRepository.findAddressById(id);
  }

  @Transactional
  public void updateUserActive(User user) {
    user.setUserStatusActive();
    userRepository.save(user);
    log.info("user update 완료");
  }
}
