package com.yapp.crew.auth;

import com.yapp.crew.errors.TokenRequiredException;
import com.yapp.crew.errors.WrongTokenPrefixException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Auth {

	@Value(value = "${jwt.secret}")
	private String jwtSecret;

	@Value(value = "${jwt.prefix}")
	private String jwtPrefix;

	public void verifyToken(String token) throws TokenRequiredException, WrongTokenPrefixException {
		if (token == null || token.isBlank()) {
			throw new TokenRequiredException("[Auth] Token is required but wasn't sent");
		}

		if (!token.startsWith("Bearer")) {
			throw new WrongTokenPrefixException("[Auth] Bearer is required for token prefix");
		}
	}

	public Long parseUserIdFromToken(String token) {
		token = token.replace(jwtPrefix + " ", "");
		Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
				.build()
				.parseClaimsJws(token);

		Claims body = claimsJws.getBody();
		return Long.parseLong(String.valueOf(body.get("userId")));
	}
}
