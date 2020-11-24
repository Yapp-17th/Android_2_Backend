package com.yapp.crew.service;

import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.BookMark;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.model.BoardListInfo;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookMarkService {

	private UserRepository userRepository;

	@Autowired
	public BookMarkService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public List<BoardListInfo> getBookMarks(long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		Set<BookMark> bookMarkSet = user.getUserBookmark();
		return bookMarkSet.stream()
				.map(bookMark -> BoardListInfo.build(bookMark.getBoard()))
				.collect(Collectors.toList());
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}
}
