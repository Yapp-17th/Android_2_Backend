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
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class JwtUtils {

  private String secret;
  private String header;
  private int expiration;

  public JwtUtils(String secret, String header, int expiration) {
    this.secret = secret;
    this.header = header;
    this.expiration = expiration;
  }

  public String getHeader() {
    return this.header;
  }

  public String createToken(User user) throws Exception {
    log.info("userId: " + user.getId());
    log.info("oauthId: " + user.getOauthId());
    log.info("accessToken: " + user.getAccessToken());

    String token = Jwts.builder()
        .setExpiration(generateExpirationDate())
        .setIssuedAt(new Date())
        .setId(UUID.randomUUID().toString())
        .claim("userId", String.valueOf(user.getId()))
        .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
        .compact();

    log.info("create token:" + token);
    return token;
  }

  private Date generateExpirationDate() {
    Date now = new Date();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(now);
    calendar.add(Calendar.DAY_OF_WEEK, expiration);
    return calendar.getTime();
  }

  public Boolean validateToken(String token, User user) {
    final String userId = getUserIdFromToken(token);
    return (userId.equals(String.valueOf(user.getId()))) && !(isTokenExpired(token));
  }

  public boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date(System.currentTimeMillis()));
  }

  private String getUserIdFromToken(String token) {
    String userId;
    try {
      final Claims claims = getClaimsFromToken(token);
      userId = claims.getSubject();
    } catch (Exception e) {
      userId = null;
    }
    return userId;
  }

  private Date getExpirationDateFromToken(String token) {
    Date expiration;
    try {
      final Claims claims = getClaimsFromToken(token);
      expiration = claims.getExpiration();
    } catch (Exception e) {
      expiration = null;
    }
    return expiration;
  }

  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parserBuilder()
          .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }
}
