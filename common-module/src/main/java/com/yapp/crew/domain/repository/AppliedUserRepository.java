package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.AppliedUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedUserRepository extends JpaRepository<AppliedUser, Long> {
}
