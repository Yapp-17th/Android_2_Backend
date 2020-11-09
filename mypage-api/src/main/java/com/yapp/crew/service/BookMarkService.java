package com.yapp.crew.service;

import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.model.BookMark;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.repository.BookMarkRepository;
import com.yapp.crew.domain.repository.UserRepository;
import com.yapp.crew.model.BoardListInfo;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BookMarkService {

	private UserRepository userRepository;
	private BookMarkRepository bookMarkRepository;

	@Autowired
	public BookMarkService(UserRepository userRepository, BookMarkRepository bookMarkRepository) {
		this.userRepository = userRepository;
		this.bookMarkRepository = bookMarkRepository;
	}

	@Transactional
	public List<BoardListInfo> getBookMarks(long userId) {
		User user = findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("user not found"));

		Set<BookMark> bookMarkSet = user.getUserBookmark();
		return bookMarkSet.stream()
				.map(bookMark -> BoardListInfo.build(bookMark.getBoard(), user))
				.collect(Collectors.toList());
	}

	private Optional<User> findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}
}
