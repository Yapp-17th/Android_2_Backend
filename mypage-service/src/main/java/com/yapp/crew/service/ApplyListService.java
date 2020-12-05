package com.yapp.crew.service;

import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.ChatRoom;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.repository.ChatRoomRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.model.ApplyListInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplyListService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final ChatRoomRepository chatRoomRepository;

	@Autowired
	public ApplyListService(UserRepository userRepository, BoardRepository boardRepository, ChatRoomRepository chatRoomRepository) {
		this.userRepository = userRepository;
		this.boardRepository = boardRepository;
		this.chatRoomRepository = chatRoomRepository;
	}

	@Transactional
	public List<ApplyListInfo> getApplyList(long hostId, long boardId) {
		Board board = findBoardById(boardId)
				.orElseThrow(() -> new BoardNotFoundException(boardId));

		User host = userRepository.findUserById(hostId)
				.orElseThrow(() -> new UserNotFoundException(hostId));

		return board.getAppliedUsers().stream()
				.map(appliedUser -> {
					boolean isNewRoom = false;
					ChatRoom newChatRoom = null;
					Optional<ChatRoom> chatRoom = findChatRoomByBoardIdAndGuestId(appliedUser.getUser().getId(), boardId);
					if (chatRoom.isEmpty()) {
						newChatRoom = ChatRoom.buildChatRoom(host, appliedUser.getUser(), board);
						chatRoomRepository.save(newChatRoom);
						isNewRoom = true;
					}
					else if (chatRoom.get().getHostExited()) {
						chatRoom.get().inviteUser(true);
						chatRoomRepository.save(chatRoom.get());
					}
					else if (chatRoom.get().getGuestExited()) {
						chatRoom.get().inviteUser(false);
						chatRoomRepository.save(chatRoom.get());
					}

					if (isNewRoom) {
						return ApplyListInfo.build(
								hostId,
								appliedUser.getUser(),
								board,
								newChatRoom
						);
					}
					else {
						return ApplyListInfo.build(
								hostId,
								appliedUser.getUser(),
								board,
								chatRoom.get()
						);
					}
				})
				.collect(Collectors.toList());
	}

	private Optional<Board> findBoardById(Long boardId) {
		return boardRepository.findBoardById(boardId);
	}

	private Optional<ChatRoom> findChatRoomByBoardIdAndGuestId(Long guestId, Long boardId) {
		return chatRoomRepository.findByGuestIdAndBoardId(guestId, boardId);
	}
}
