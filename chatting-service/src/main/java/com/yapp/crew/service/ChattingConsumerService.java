package com.yapp.crew.service;

import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChattingConsumerService {

  private final ChatRoomRepository chatRoomRepository;

  private final MessageRepository messageRepository;

  @Autowired
	public ChattingConsumerService(
					ChatRoomRepository chatRoomRepository,
					MessageRepository messageRepository
	) {
  	this.chatRoomRepository = chatRoomRepository;
  	this.messageRepository = messageRepository;
	}

  public void processMessage(Message message) {
    switch (message.getType()) {
      case ENTER:
        saveMessage(message);
        log.info("Successfully sent welcome bot message");
        break;
      case TALK:
        saveMessage(message);
        log.info("Successfully sent message");
        break;
			case BOT_MESSAGE:
				saveMessage(message);
				log.info("Successfully sent bot message");
				break;
      case PROFILE_REQUEST:
        // request profile logic
        break;
      default:
        log.info("Invalid Message Type Received");
    }
  }

  private void saveMessage(Message message) {
    log.info("Successfully persisted message");
    messageRepository.save(message);
  }
}
