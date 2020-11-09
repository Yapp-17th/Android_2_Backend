package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.response.UserProfileResponseDto;
import com.yapp.crew.model.UserProfileInfo;
import com.yapp.crew.service.MyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyProfileController {

	private MyProfileService myProfileService;
	private Auth auth;

	@Autowired
	public MyProfileController(MyProfileService myProfileService, Auth auth) {
		this.myProfileService = myProfileService;
		this.auth = auth;
	}

	@GetMapping(path = "/v1/user/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyProfile(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
		long userId = auth.parseUserIdFromToken(token);

		UserProfileInfo userProfileInfo = myProfileService.getProfile(userId);
		UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.build(userProfileInfo);

		return ResponseEntity.ok().body(userProfileResponseDto);
	}


}
