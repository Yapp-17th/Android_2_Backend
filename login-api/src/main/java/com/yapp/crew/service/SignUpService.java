package com.yapp.crew.service;

import com.yapp.crew.domain.model.Address;
import com.yapp.crew.domain.model.Category;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.model.User.UserBuilder;
import com.yapp.crew.domain.repository.AddressRepository;
import com.yapp.crew.domain.repository.CategoryRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.type.ExerciseType;
import com.yapp.crew.dto.LoginResponseDto;
import com.yapp.crew.model.SignupUserInfo;
import com.yapp.crew.utils.ResponseMessage;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignUpService {

  private UserRepository userRepository;
  private CategoryRepository categoryRepository;
  private AddressRepository addressRepository;

  @Autowired
  public SignUpService(UserRepository userRepository, CategoryRepository categoryRepository, AddressRepository addressRepository) {
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.addressRepository = addressRepository;
  }

  public LoginResponseDto signUp(SignupUserInfo signupUserInfo) {
    try {
      UserBuilder userBuilder = User.getBuilder();

      Optional<Category> category = findCategoryById(signupUserInfo.getCategory());
      Optional<Address> address = findAddressById(signupUserInfo.getAddress());

      User user = userBuilder
          .withOauthId(signupUserInfo.getOauthId())
          .withAccessToken(signupUserInfo.getAccessToken())
          .withEmail(signupUserInfo.getEmail())
          .withNickname(signupUserInfo.getNickName())
          .withUsername(signupUserInfo.getUserName())
          .withCategory(category.get())
          .withAddress(address.get())
          .withIntro(signupUserInfo.getIntro())
          .build();
      saveUser(user);

      return LoginResponseDto.pass(ResponseMessage.SIGNUP_SUCCESS.getMessage());
    } catch (Exception e) {
      log.info(e.getMessage());
      return LoginResponseDto.fail(ResponseMessage.SIGNUP_FAIL.getMessage());
    }
  }

  private void saveUser(User user) {
    userRepository.save(user);
    log.info("user login 성공");
  }

  private Optional<Category> findCategoryById(Long id) {
    log.info("find category by id");
    return categoryRepository.findCategoryById(id);
  }

  private Optional<Address> findAddressById(Long id) {
    log.info("find address by id");
    return addressRepository.findAddressById(id);
  }
}
