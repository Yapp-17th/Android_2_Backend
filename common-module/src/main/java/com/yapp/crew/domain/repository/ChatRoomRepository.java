package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  List<ChatRoom> findAll();

  List<ChatRoom> findAllByHostId(Long userId);

  List<ChatRoom> findAllByGuestId(Long userId);

  ChatRoom save(ChatRoom chatRoom);
}
