package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findAll();

  Optional<User> findUserById(Long userId);

  User save(User user);
}
