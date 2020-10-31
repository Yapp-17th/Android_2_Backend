package com.yapp.crew.domain.auth;

import com.yapp.crew.domain.errors.TokenRequiredException;
import com.yapp.crew.domain.errors.WrongTokenPrefixException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Auth {

	@Value(value = "${jwt.secret}")
	private String jwtSecret;

	public void verifyToken(String token) {
		if (token == null) {
			throw new TokenRequiredException("[Auth] Token is required but wasn't sent");
		}

		if (!token.startsWith("Bearer")) {
			throw new WrongTokenPrefixException("[Auth] Bearer is required for token prefix");
		}

		// verify expiration
	}

	public Long parseUserIdFromToken(String token) {
		Jws<Claims> claimsJws = Jwts.parserBuilder()
						.setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
						.build()
						.parseClaimsJws(token);

		Claims body = claimsJws.getBody();
		return Long.parseLong(String.valueOf(body.get("userId")));
	}
}
