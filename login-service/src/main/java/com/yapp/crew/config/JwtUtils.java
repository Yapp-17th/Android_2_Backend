package com.yapp.crew.config;

import com.yapp.crew.domain.errors.TokenRequiredException;
import com.yapp.crew.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtils {

	@Value(value = "${jwt.secret}")
	private String secret;

	@Value(value = "${jwt.expiration}")
	private String expiration;

	@Value(value = "${jwt.header}")
	private String header;

	@Value(value = "${jwt.prefix}")
	private String prefix;

	public String getHeader() {
		return this.header;
	}

	public String createToken(User user) {
		log.info("userId: " + user.getId());
		Date now = new Date();

		String token = Jwts.builder()
				.setExpiration(generateExpirationDate(now))
				.setIssuedAt(now)
				.setId(UUID.randomUUID().toString())
				.claim("userId", String.valueOf(user.getId()))
				.signWith(Keys.hmacShaKeyFor(secret.getBytes()))
				.compact();

		log.info("create token: " + token);
		return token;
	}

	private Date generateExpirationDate(Date now) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_WEEK, Integer.parseInt(expiration));
		return calendar.getTime();
	}

	public long getUserIdFromToken(String token) {
		token = token.replace(prefix + " ", "");
		log.info("remove prefix token: " + token);

		try {
			final Claims claims = getClaimsFromToken(token).getBody();
			long userId = Long.parseLong(String.valueOf(claims.get("userId")));

			log.info("userId from token: " + userId);
			return userId;
		} catch (Exception e) {
			throw new TokenRequiredException("[Auto Login Service] Token is required but wasn't sent");
		}
	}

	public Date getExpirationFromToken(String token) {
		token = token.replace(prefix + " ", "");
		log.info("remove prefix token: " + token);

		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token).getBody();
			expiration = claims.getExpiration();
			log.info("expiration from token: " + expiration.toString());
		} catch (Exception e) {
			expiration = new Date();
		}
		return expiration;
	}

	private Jws<Claims> getClaimsFromToken(String token) {
		Jws<Claims> claims;
		try {
			claims = Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
					.build()
					.parseClaimsJws(token);
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public boolean verifyToken(String token) {
		if (token == null || token.isBlank()) {
			return false;
		}

		if (!token.startsWith("Bearer")) {
			return false;
		}

		Date expiration = getExpirationFromToken(token);
		return expiration.before(new Date());
	}
}
