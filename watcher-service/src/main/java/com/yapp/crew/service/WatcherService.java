package com.yapp.crew.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.repository.BoardRepository;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.payload.BoardFinishedPayload;
import com.yapp.crew.producer.WatcherProducer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "Watcher Service")
@Service
public class WatcherService {

	private final WatcherProducer watcherProducer;

	private final BoardRepository boardRepository;

	private final BoardBatchService boardBatchService;

	private final UserAuthenticationService userAuthenticationService;

	@Autowired
	public WatcherService(
			WatcherProducer watcherProducer,
			BoardRepository boardRepository,
			BoardBatchService boardBatchService,
			UserAuthenticationService userAuthenticationService
	) {
		this.watcherProducer = watcherProducer;
		this.boardRepository = boardRepository;
		this.boardBatchService = boardBatchService;
		this.userAuthenticationService = userAuthenticationService;
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void boardSuccessfullyFinishedWatcher() {
		log.info("Watcher start...");
		LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		List<Board> boards = boardRepository.findAllBoardByExpiredStatus(now.minusDays(1), now);
		log.info("Boards that needs an update -> {}", boards);

		if (!boards.isEmpty()) {
			boards.forEach(board -> {
				try {
					BoardFinishedPayload payload = BoardFinishedPayload.builder()
							.boardId(board.getId())
							.build();
					log.info("Produce board successfully finished event");
					watcherProducer.produceBoardSuccessfullyFinishedEvent(payload);
					boardBatchService.saveEvaluationList(board);
				} catch (JsonProcessingException ex) {
					// TODO: handle exception
					ex.printStackTrace();
				}
			});
			boardBatchService.updateBoardFinishedAll(boards);
			log.info("finished update board!");
		}
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void userAuthenticationWatcher() {
		log.info("User Authentication Watcher start");
		userAuthenticationService.countUserSuspendedDays();
	}
}
