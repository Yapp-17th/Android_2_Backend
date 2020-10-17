package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findAll();

  List<Message> findAllByChatRoomId(Long chatRoomId);

  List<Message> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

  Message save(Message message);
}
