package com.yapp.crew.controller;

import com.yapp.crew.dto.request.UserUpdateRequestDto;
import com.yapp.crew.dto.response.HistoryListResponseDto;
import com.yapp.crew.dto.response.UserProfileResponseDto;
import com.yapp.crew.model.HistoryListInfo;
import com.yapp.crew.model.UserProfileInfo;
import com.yapp.crew.model.UserUpdateRequest;
import com.yapp.crew.network.dto.SimpleResponseDto;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.MyProfileService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyProfileController {

	private MyProfileService myProfileService;

	@Autowired
	public MyProfileController(MyProfileService myProfileService) {
		this.myProfileService = myProfileService;
	}

	@GetMapping(path = "/v1/user/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyProfile(@RequestHeader(value = "userId") Long userId) {
		UserProfileInfo userProfileInfo = myProfileService.getProfile(userId);
		UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.build(userProfileInfo);

		return ResponseEntity.ok().body(userProfileResponseDto);
	}

	@PutMapping(path = "/v1/user/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMyProfile(
			@RequestHeader(value = "userId") Long userId,
			@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto
	) {
		UserUpdateRequest userUpdateRequest = UserUpdateRequest.build(userUpdateRequestDto);

		SimpleResponse simpleResponse = myProfileService.updateProfile(userId, userUpdateRequest);

		return ResponseEntity.ok().body(SimpleResponseDto.build(simpleResponse));
	}

	@GetMapping(path = "/v1/user/my-profile/history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyHistory(
			@RequestHeader(value = "userId") Long userId,
			@RequestParam(required = false, defaultValue = "continue") String type
	) {
		List<HistoryListInfo> historyListInfos = myProfileService.getHistoryList(userId, type);
		HistoryListResponseDto historyListResponseDto = HistoryListResponseDto.build(type, historyListInfos);

		return ResponseEntity.ok().body(historyListResponseDto);
	}
}
