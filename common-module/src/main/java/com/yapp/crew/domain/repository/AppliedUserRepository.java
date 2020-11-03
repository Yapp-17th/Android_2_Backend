package com.yapp.crew.domain.repository;

import java.util.Optional;

import com.yapp.crew.domain.model.AppliedUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedUserRepository extends JpaRepository<AppliedUser, Long> {

	Optional<AppliedUser> findByBoardIdAndUserId(Long boardId, Long userId);
}
