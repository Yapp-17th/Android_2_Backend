package com.yapp.crew.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.Message;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.MessageRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.domain.type.MessageType;
import com.yapp.crew.payload.ChatRoomPayload;
import com.yapp.crew.payload.MessagePayload;
import com.yapp.crew.producer.ChattingProducer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChattingProducerService {

  @Autowired
  private ChatRoomRepository chatRoomRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private ChattingProducer chattingProducer;

  public void createChatRoom(ChatRoomPayload chatRoomPayload) throws JsonProcessingException {
    User host = userRepository.findUserById(chatRoomPayload.getHostId())
            .orElseThrow(() -> new RuntimeException("Cannot find host user with id"));

    User guest = userRepository.findUserById(chatRoomPayload.getGuestId())
            .orElseThrow(() -> new RuntimeException("Cannot find guest user"));

    Board board = boardRepository.findById(chatRoomPayload.getBoardId())
            .orElseThrow(() -> new RuntimeException("Cannot find board"));

    ChatRoom chatRoom = ChatRoom.getBuilder()
            .withHost(host)
            .withGuest(guest)
            .withBoard(board)
            .build();

    chatRoomRepository.save(chatRoom);
    log.info("Successfully created a new chat room");

    sendWelcomeBotMessage(chatRoom.getId());
  }

  public List<Message> receiveChatMessages(Long chatRoomId) {
    return messageRepository.findAllByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
  }

  private void sendWelcomeBotMessage(Long chatRoomId) throws JsonProcessingException {
    User bot = userRepository.findUserById(-1L)
            .orElseThrow(() -> new RuntimeException("Cannot find bot"));

    MessagePayload welcomeBotMessage = MessagePayload.builder()
            .content("봇 환영 메시지")
            .type(MessageType.BOT_MESSAGE)
            .senderId(bot.getId())
            .chatRoomId(chatRoomId)
            .build();

    chattingProducer.sendMessage(welcomeBotMessage);
  }
}
