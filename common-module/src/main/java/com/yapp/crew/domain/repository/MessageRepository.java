package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findAll();

	List<Message> findAllByChatRoomId(Long chatRoomId);

	List<Message> findAllByChatRoomIdOrderByCreatedAt(Long chatRoomId);

	Optional<Message> findFirstByChatRoomIdOrderByIdDesc(Long chatRoomId);

	Message save(Message message);
}
