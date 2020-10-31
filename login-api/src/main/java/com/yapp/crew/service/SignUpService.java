package com.yapp.crew.service;

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
import com.yapp.crew.model.LoginResponse;
import com.yapp.crew.model.LoginResponseBody;
import com.yapp.crew.model.SignupUserInfo;
import com.yapp.crew.utils.ResponseMessage;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

  public LoginResponse signUp(SignupUserInfo signupUserInfo) {
    try {
      UserBuilder userBuilder = User.getBuilder();

      Optional<Address> address = findAddressById(signupUserInfo.getAddress());

      User user = userBuilder
          .withOauthId(signupUserInfo.getOauthId())
          .withAccessToken(signupUserInfo.getAccessToken())
          .withEmail(signupUserInfo.getEmail())
          .withNickname(signupUserInfo.getNickName())
          .withUsername(signupUserInfo.getUserName())
          .withAddress(address.get())
          .withIntro(signupUserInfo.getIntro())
          .build();

      // TODO: transaction 처리 추가 필요
      save(user, signupUserInfo.getCategory());

      HttpHeaders httpHeaders = tokenService.setToken(user);
      LoginResponseBody loginResponseBody = LoginResponseBody.pass(ResponseMessage.SIGNUP_SUCCESS.getMessage());

      return new LoginResponse(httpHeaders, loginResponseBody);
    } catch (Exception e) {
      log.info(e.getMessage());
      LoginResponseBody loginResponseBody = LoginResponseBody.fail(ResponseMessage.SIGNUP_FAIL.getMessage());
      return new LoginResponse(loginResponseBody);
    }
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

  private void save(User user, List<Long> category) throws Exception {
    saveUser(user);
    Optional<User> savedUser = findUserByOauthId(user.getOauthId());

    for (long categoryId : category) {
      Optional<Category> userCategory = findCategoryById(categoryId);

      if (savedUser.isPresent() && userCategory.isPresent()) {
        saveUserExercise(savedUser.get(), userCategory.get());
      } else {
        throw new Exception("user 또는 category가 존재하지 않습니다.");
      }
    }
  }

  private Optional<Category> findCategoryById(Long id) {
    log.info("find category by id");
    return categoryRepository.findCategoryById(id);
  }

  private Optional<User> findUserByOauthId(String oauthId) {
    log.info("find user by id");
    return userRepository.findByOauthId(oauthId);
  }

  private Optional<Address> findAddressById(Long id) {
    log.info("find address by id");
    return addressRepository.findAddressById(id);
  }
}
