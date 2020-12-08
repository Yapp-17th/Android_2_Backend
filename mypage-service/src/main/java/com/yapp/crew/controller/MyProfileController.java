package com.yapp.crew.controller;

import com.yapp.crew.dto.request.UserUpdateRequestDto;
import com.yapp.crew.dto.response.HistoryListResponseDto;
import com.yapp.crew.dto.response.UserEditProfileResponseDto;
import com.yapp.crew.dto.response.UserProfileResponseDto;
import com.yapp.crew.model.HistoryListInfo;
import com.yapp.crew.model.UserInfo;
import com.yapp.crew.model.UserProfileInfo;
import com.yapp.crew.model.UserUpdateRequest;
import com.yapp.crew.network.dto.SimpleResponseDto;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.MyProfileService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "My Profile Controller")
@RestController
public class MyProfileController {

	private MyProfileService myProfileService;

	@Autowired
	public MyProfileController(MyProfileService myProfileService) {
		this.myProfileService = myProfileService;
	}

	@GetMapping(path = "/v1/user/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyProfile(@RequestHeader(value = "userId") long userId) {
		log.info("Get my profile -> userId: {}", userId);
		UserProfileInfo userProfileInfo = myProfileService.getProfile(userId);
		UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.build(userProfileInfo);

		return ResponseEntity.ok().body(userProfileResponseDto);
	}

	@GetMapping(path = "/v1/user/my-profile/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEditMyProfile(@RequestHeader(value = "userId") long userId) {
		log.info("Get my edit profile -> userId: {}", userId);
		UserInfo userInfo = myProfileService.getEditProfile(userId);
		UserEditProfileResponseDto userEditProfileResponseDto = UserEditProfileResponseDto.build(userInfo);

		return ResponseEntity.ok().body(userEditProfileResponseDto);
	}

	@PutMapping(path = "/v1/user/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMyProfile(
			@RequestHeader(value = "userId") long userId,
			@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto
	) {
		log.info("Update my profile -> userId: {}, payload: {}", userId, userUpdateRequestDto);
		UserUpdateRequest userUpdateRequest = UserUpdateRequest.build(userUpdateRequestDto);

		SimpleResponse simpleResponse = myProfileService.updateProfile(userId, userUpdateRequest);

		return ResponseEntity.ok().body(SimpleResponseDto.build(simpleResponse));
	}

	@GetMapping(path = "/v1/user/my-profile/history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyHistory(
			@RequestHeader(value = "userId") long userId,
			@RequestParam(required = false, defaultValue = "continue") String type
	) {
		log.info("Get my history -> userId: {}, type: {}", userId, type);
		List<HistoryListInfo> historyListInfos = myProfileService.getHistoryList(userId, type);
		HistoryListResponseDto historyListResponseDto = HistoryListResponseDto.build(type, historyListInfos);

		return ResponseEntity.ok().body(historyListResponseDto);
	}
}
