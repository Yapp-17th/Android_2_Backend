package com.yapp.crew.domain.repository;

import java.util.List;
import java.util.Optional;

import com.yapp.crew.domain.model.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  List<ChatRoom> findAll();

  List<ChatRoom> findAllByHostId(Long hostId);

  List<ChatRoom> findAllByGuestId(Long guestId);

	@Query(value = "select * from chat_room where host_id = ?1 or guest_id = ?1", nativeQuery = true)
	List<ChatRoom> findAllByUserId(Long userId);

  Optional<ChatRoom> findByGuestIdAndBoardId(Long guestId, Long boardId);

  ChatRoom save(ChatRoom chatRoom);
}
