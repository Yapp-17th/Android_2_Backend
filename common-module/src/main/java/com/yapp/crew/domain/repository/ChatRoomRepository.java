package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	List<ChatRoom> findAll();

	List<ChatRoom> findAllByHostId(Long hostId);

	List<ChatRoom> findAllByGuestId(Long guestId);

	@Query(value = "select * from chat_room where board_id = ?1 and status = 'ACTIVE'", nativeQuery = true)
	List<ChatRoom> findAllByBoardId(Long boardId);

	@Query(value = "select * from chat_room where (host_id = ?1 or guest_id = ?1) and status = 'ACTIVE' order by updated_at desc", nativeQuery = true)
	List<ChatRoom> findAllByUserId(Long userId);

	@Query(value = "select * from chat_room where guest_id = ?1 and board_id = ?2 and status = 'ACTIVE'", nativeQuery = true)
	Optional<ChatRoom> findByGuestIdAndBoardId(Long guestId, Long boardId);

	ChatRoom save(ChatRoom chatRoom);
}
