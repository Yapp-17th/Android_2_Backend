package com.yapp.crew.domain.repository;

import java.util.List;
import java.util.Optional;

import com.yapp.crew.domain.model.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  List<ChatRoom> findAll();

  List<ChatRoom> findAllByHostId(Long hostId);

  List<ChatRoom> findAllByGuestId(Long guestId);

  Optional<ChatRoom> findByGuestIdAndBoardId(Long guestId, Long boardId);

  ChatRoom save(ChatRoom chatRoom);
}
