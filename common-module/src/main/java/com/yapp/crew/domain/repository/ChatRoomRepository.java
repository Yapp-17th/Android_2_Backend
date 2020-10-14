package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  List<ChatRoom> findAll();

  List<ChatRoom> findAllByHostId(Long userId);

  List<ChatRoom> findAllByGuestId(Long userId);

  ChatRoom save(ChatRoom chatRoom);
}
