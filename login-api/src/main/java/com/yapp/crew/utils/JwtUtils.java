package com.yapp.crew.utils;

import com.yapp.crew.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtils {

  private static final String secret = "secretkeytest1234secretkeytest1234";
  private static final int expiration = 1;
  private static final String header = "X-Auth-Token";

  public static String getHeader() {
    return header;
  }

  public static String createToken(User user) throws Exception {
    log.info("userId: " + user.getId());
    log.info("oauthId: " + user.getOauthId());
    log.info("accessToken: " + user.getAccessToken());

    String token = Jwts.builder()
        .setExpiration(generateExpirationDate())
        .setIssuedAt(new Date())
        .setId(UUID.randomUUID().toString())
        .claim("userId", String.valueOf(user.getId()))
        .claim("oauthId", String.valueOf(user.getOauthId()))
        .claim("accessToken", user.getAccessToken())
        .signWith(SignatureAlgorithm.HS256, Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
        .compact();

    log.info("create token:" + token);
    return token;
  }

  private static Date generateExpirationDate() {
    Date now = new Date();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(now);
    calendar.add(Calendar.DAY_OF_WEEK, expiration);
    return calendar.getTime();
  }

  public static Boolean validateToken(String token, User user) {
    final String userId = getUserIdFromToken(token);
    return (userId.equals(String.valueOf(user.getId()))) && !(isTokenExpired(token));
  }

  public static boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date(System.currentTimeMillis()));
  }

  private static String getUserIdFromToken(String token) {
    String userId;
    try {
      final Claims claims = getClaimsFromToken(token);
      userId = claims.getSubject();
    } catch (Exception e) {
      userId = null;
    }
    return userId;
  }

  private static Date getExpirationDateFromToken(String token) {
    Date expiration;
    try {
      final Claims claims = getClaimsFromToken(token);
      expiration = claims.getExpiration();
    } catch (Exception e) {
      expiration = null;
    }
    return expiration;
  }

  private static Claims getClaimsFromToken(String token) {
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
