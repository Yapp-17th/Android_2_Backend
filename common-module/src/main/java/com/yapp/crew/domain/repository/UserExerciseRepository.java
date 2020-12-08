package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.UserExercise;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExerciseRepository extends JpaRepository<UserExercise, Long> {

	List<UserExercise> findAll();

	List<UserExercise> findAllByUserId(long userId);

	UserExercise save(UserExercise userExercise);
}
