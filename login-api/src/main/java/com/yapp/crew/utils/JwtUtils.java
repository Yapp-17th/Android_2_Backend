package com.yapp.crew.utils;

import com.yapp.crew.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JwtUtils {

  private Key key;
  private int expiration;
  private String header;

  public JwtUtils(String secret, int expiration, String header) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.expiration = expiration;
    this.header = header;
  }

  public String getHeader(){
    return header;
  }

  public String createToken(User user) {
    return Jwts.builder()
        .claim("userId", user.getId())
        .claim("oauthId", user.getOauthId())
        .claim("accessToken", user.getAccessToken())
        .setExpiration(generateExpirationDate())
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  private Date generateExpirationDate() {
    Date now = new Date();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(now);
    calendar.add(Calendar.DAY_OF_WEEK, expiration);
    return calendar.getTime();
  }

  public Boolean validateToken(String token, User user) {
    final String userId = this.getUserIdFromToken(token);
    final Date expiration = this.getExpirationDateFromToken(token);
    return (userId.equals(String.valueOf(user.getId()))) && !(isTokenExpired(token));
  }

  public boolean isTokenExpired(String token) {
    final Date expiration = this.getExpirationDateFromToken(token);
    return expiration.before(new Date(System.currentTimeMillis()));
  }

  private String getUserIdFromToken(String token) {
    String userId;
    try {
      final Claims claims = this.getClaimsFromToken(token);
      userId = claims.getSubject();
    } catch (Exception e) {
      userId = null;
    }
    return userId;
  }

  private Date getExpirationDateFromToken(String token) {
    Date expiration;
    try {
      final Claims claims = this.getClaimsFromToken(token);
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
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }
}
