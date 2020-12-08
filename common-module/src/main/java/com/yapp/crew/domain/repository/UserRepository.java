package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findAll();

	Optional<User> findUserById(long userId);

	User save(User user);

	Optional<User> findByOauthId(String oauthId);

	@Query(value = "select * from user where status = 'SUSPENDED'", nativeQuery = true)
	List<User> findSuspendedUsers();
}
