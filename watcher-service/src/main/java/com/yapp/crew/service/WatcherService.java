package com.yapp.crew.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.payload.BoardFinishedPayload;
import com.yapp.crew.producer.WatcherProducer;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WatcherService {

	private final WatcherProducer watcherProducer;

	private final BoardRepository boardRepository;

	@Autowired
	public WatcherService(
			WatcherProducer watcherProducer,
			BoardRepository boardRepository
	) {
		this.watcherProducer = watcherProducer;
		this.boardRepository = boardRepository;
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void boardSuccessfullyFinishedWatcher() {
		List<Board> boards = boardRepository.findAllByStartsAtBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now());

		if (!boards.isEmpty()) {
			boards.stream()
				.filter(board -> !board.getStatus().equals(BoardStatus.CANCELED) && !board.getStatus().equals(BoardStatus.FINISHED))
				.forEach(board -> {
					try {
							BoardFinishedPayload payload = BoardFinishedPayload.builder()
									.boardId(board.getId())
									.build();

							watcherProducer.produceBoardSuccessfullyFinishedEvent(payload);
						} catch (JsonProcessingException ex) {
							// TODO: handle exception
							ex.printStackTrace();
						}
					});
		}
	}
}
