package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findAll();

  List<Message> findAllByChatRoomId(Long chatRoomId);

  Message save(Message message);
}
