package com.yapp.crew.controller;

import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.dto.request.LoginRequestDto;
import com.yapp.crew.model.LoginUserInfo;
import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.network.dto.SimpleResponseDto;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.SignInService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "SignIn Controller")
@CrossOrigin
@RestController
public class SignInController {

	private SignInService signInService;

	@Autowired
	public SignInController(SignInService signInService) {
		this.signInService = signInService;
	}

	@PostMapping(path = "/v1/user/sign-in", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postSignIn(@RequestBody @Valid LoginRequestDto loginRequestDto) {
		log.info("Post sign in -> payload: {}", loginRequestDto);

		LoginUserInfo loginUserInfo = new LoginUserInfo(loginRequestDto.getUserId());
		UserAuthResponse userAuthResponse = signInService.signIn(loginUserInfo);

		HttpHeaders httpHeaders = userAuthResponse.getHttpHeaders();
		SimpleResponseDto simpleResponseDto = SimpleResponseDto.build(userAuthResponse.getSimpleResponse());
		if (httpHeaders != null) {
			return ResponseEntity.ok().headers(httpHeaders).body(simpleResponseDto);
		}

		return ResponseEntity.ok()
				.body(SimpleResponseDto.build(SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL)));
	}

	@GetMapping(path = "/v1/user/auto-in", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAutoSignIn(@RequestHeader(value = "Authorization") String token) {
		log.info("auto log in -> header: {}", token);
		UserAuthResponse userAuthResponse = signInService.autoSignIn(token);

		HttpHeaders httpHeaders = userAuthResponse.getHttpHeaders();
		SimpleResponseDto simpleResponseDto = SimpleResponseDto.build(userAuthResponse.getSimpleResponse());
		if (httpHeaders != null) {
			return ResponseEntity.ok().headers(httpHeaders).body(simpleResponseDto);
		}

		return ResponseEntity.ok()
				.body(SimpleResponseDto.build(SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL)));
	}
}
