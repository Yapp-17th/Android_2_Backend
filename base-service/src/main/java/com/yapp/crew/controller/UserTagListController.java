package com.yapp.crew.controller;

import com.yapp.crew.dto.EnumListResponseDto;
import com.yapp.crew.utils.EnumToList;
import com.yapp.crew.utils.ResponseDomain;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "User Tag List Controller")
@RestController
public class UserTagListController {

	@GetMapping(path = "/v1/user/tag")
	public ResponseEntity<?> getUserTagList() {
		log.info("Get User Tag List");

		List<String> userTagList = EnumToList.userTypeEnumToList();
		EnumListResponseDto enumListResponseDto = EnumListResponseDto.pass(ResponseDomain.USER_TAG.getName(), userTagList);

		return ResponseEntity.ok().body(enumListResponseDto);
	}
}
