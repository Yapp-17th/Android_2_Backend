package com.yapp.crew.controller;

import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.dto.request.SignUpRequestDto;
import com.yapp.crew.model.SignupUserInfo;
import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.network.dto.SimpleResponseDto;
import com.yapp.crew.network.model.SimpleResponse;
import com.yapp.crew.service.SignUpService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "SignUp Controller")
@CrossOrigin
@RestController
public class SignUpController {

	private SignUpService signUpService;

	@Autowired
	public SignUpController(SignUpService signUpService) {
		this.signUpService = signUpService;
	}

	@PostMapping(path = "/v1/user/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postSignIn(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
		log.info("Post sign in -> payload: {}", signUpRequestDto);

		SignupUserInfo signupUserInfo = SignupUserInfo.build(signUpRequestDto);
		UserAuthResponse userAuthResponse = signUpService.signUp(signupUserInfo);

		SimpleResponseDto simpleResponseDto = SimpleResponseDto.build(userAuthResponse.getSimpleResponse());
		HttpHeaders httpHeaders = userAuthResponse.getHttpHeaders();

		if (httpHeaders != null) {
			return ResponseEntity.ok().headers(httpHeaders).body(simpleResponseDto);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(SimpleResponseDto.build(SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL)));
	}
}
