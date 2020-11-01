package com.yapp.crew.domain.repository;

import java.util.List;
import java.util.Optional;

import com.yapp.crew.domain.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findAll();

  Optional<User> findUserById(Long userId);

  User save(User user);

  Optional<User> findByOauthId(String oauthId);

}
