package com.yapp.crew.service;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.model.ApplyListInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ApplyListService {

	private BoardRepository boardRepository;
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	public ApplyListService(BoardRepository boardRepository, ChatRoomRepository chatRoomRepository) {
		this.boardRepository = boardRepository;
		this.chatRoomRepository = chatRoomRepository;
	}

	@Transactional
	public List<ApplyListInfo> getApplyList(long hostId, long boardId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException("board not found"));

		return board.getAppliedUsers().stream()
				.map(appliedUser -> ApplyListInfo.build(hostId, appliedUser.getUser(), board, findChatRoomByBoardIdAndGuestId(appliedUser.getUser().getId(), boardId).orElseThrow(() -> new ChatRoomNotFoundException("chatroom not found")).getId()))
				.collect(Collectors.toList());
	}

	private Optional<Board> findBoardById(Long boardId) {
		return boardRepository.findBoardById(boardId);
	}

	private Optional<ChatRoom> findChatRoomByBoardIdAndGuestId(Long guestId, Long boardId) {
		return chatRoomRepository.findByGuestIdAndBoardId(guestId, boardId);
	}
}
