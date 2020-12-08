package com.yapp.crew.config;

import com.yapp.crew.domain.model.User;
import io.jsonwebtoken.Claims;
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

	public String getUserIdFromToken(String token) {
		token = token.replace(prefix + " ", "");
		log.info("remove prefix token: " + token);

		String userId;
		try {
			final Claims claims = getClaimsFromToken(token);
			userId = String.valueOf(claims.get("userId"));
			log.info("userId from token: " + userId);
		} catch (Exception e) {
			userId = null;
		}
		return userId;
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
}
