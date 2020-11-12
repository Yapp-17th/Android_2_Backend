package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.response.HistoryListResponseDto;
import com.yapp.crew.dto.response.UserProfileResponseDto;
import com.yapp.crew.model.HistoryListInfo;
import com.yapp.crew.model.UserProfileInfo;
import com.yapp.crew.service.CommonProfileService;
import com.yapp.crew.service.MyProfileService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonProfileController {

	private CommonProfileService commonProfileService;
	private MyProfileService myProfileService;
	private Auth auth;

	@Autowired
	public CommonProfileController(CommonProfileService commonProfileService, MyProfileService myProfileService, Auth auth) {
		this.commonProfileService = commonProfileService;
		this.myProfileService = myProfileService;
		this.auth = auth;
	}

	@GetMapping(path = "/v1/user/{userId}/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCommonProfile(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable Long userId) {
		long tokenUserId = auth.parseUserIdFromToken(token);

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
	public ResponseEntity<?> getCommonHistory(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable Long userId) {

		List<HistoryListInfo> historyListInfos = commonProfileService.getHistoryList(userId);
		HistoryListResponseDto historyListResponseDto = HistoryListResponseDto.build("complete", historyListInfos);

		return ResponseEntity.ok().body(historyListResponseDto);
	}
}
