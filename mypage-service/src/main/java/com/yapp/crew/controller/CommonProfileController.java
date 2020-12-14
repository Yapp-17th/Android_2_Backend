package com.yapp.crew.controller;

import com.yapp.crew.domain.condition.HistoryCondition;
import com.yapp.crew.domain.type.HistoryType;
import com.yapp.crew.dto.response.HistoryListResponseDto;
import com.yapp.crew.dto.response.UserProfileResponseDto;
import com.yapp.crew.model.HistoryListInfo;
import com.yapp.crew.model.UserProfileInfo;
import com.yapp.crew.service.CommonProfileService;
import com.yapp.crew.service.MyProfileService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Common Profile Controller")
@RestController
public class CommonProfileController {

	private CommonProfileService commonProfileService;
	private MyProfileService myProfileService;

	@Autowired
	public CommonProfileController(CommonProfileService commonProfileService, MyProfileService myProfileService) {
		this.commonProfileService = commonProfileService;
		this.myProfileService = myProfileService;
	}

	@GetMapping(path = "/v1/user/{userId}/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCommonProfile(
			@RequestHeader(value = "userId") long tokenUserId,
			@PathVariable("userId") long userId
	) {
		log.info("Get common profile -> tokenUserId: {}, userId: {}", tokenUserId, userId);
		if (tokenUserId == userId) {
			UserProfileInfo userProfileInfo = myProfileService.getProfile(userId);
			UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.build(userProfileInfo);

			return ResponseEntity.ok().body(userProfileResponseDto);
		}
		UserProfileInfo userProfileInfo = commonProfileService.getProfile(userId);
		UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.build(userProfileInfo);

		return ResponseEntity.ok().body(userProfileResponseDto);
	}

	@GetMapping(path = "/v1/user/{userId}/profile/history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCommonHistory(
			@RequestHeader(value = "userId") long tokenUserId,
			@PageableDefault(size = 200) Pageable pageable,
			@PathVariable("userId") long userId
	) {
		log.info("Get common history -> tokenUserId: {}, userId: {}", tokenUserId, userId);

		if(tokenUserId == userId){
			List<HistoryListInfo> historyListInfos = myProfileService.getHistoryList(HistoryCondition.build(userId, HistoryType.CONTINUE), pageable);
			HistoryListResponseDto historyListResponseDto = HistoryListResponseDto.build(HistoryType.CONTINUE, historyListInfos);

			return ResponseEntity.ok().body(historyListResponseDto);
		}

		List<HistoryListInfo> historyListInfos = commonProfileService.getHistoryList(HistoryCondition.build(userId, HistoryType.COMPLETE), pageable);
		HistoryListResponseDto historyListResponseDto = HistoryListResponseDto.build(HistoryType.COMPLETE, historyListInfos);

		return ResponseEntity.ok().body(historyListResponseDto);
	}
}
