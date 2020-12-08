package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findAll();

	List<Message> findAllByIdIn(List<Long> ids);

	List<Message> findAllByChatRoomIdOrderByCreatedAt(long chatRoomId);

	List<Message> findAllByChatRoomIdAndMessageIdGreaterThan(long chatRoomId, long messageId);

	Optional<Message> findFirstByChatRoomIdOrderByIdDesc(long chatRoomId);

	Message save(Message message);
}
