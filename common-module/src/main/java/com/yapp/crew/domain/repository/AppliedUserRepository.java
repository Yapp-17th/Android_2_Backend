package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.AppliedUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedUserRepository extends JpaRepository<AppliedUser, Long> {

	Optional<AppliedUser> findByBoardIdAndUserIdAndChatRoomId(Long boardId, Long userId, Long chatRoomId);
}
