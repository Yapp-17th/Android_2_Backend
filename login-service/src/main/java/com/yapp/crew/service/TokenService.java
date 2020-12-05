package com.yapp.crew.service;

import com.yapp.crew.config.JwtUtils;
import com.yapp.crew.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

	private JwtUtils jwtUtil;

	@Autowired
	public TokenService(JwtUtils jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	public HttpHeaders setToken(User user) {
		String accessToken = jwtUtil.createToken(user);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("token", accessToken);

		return responseHeaders;
	}
}
