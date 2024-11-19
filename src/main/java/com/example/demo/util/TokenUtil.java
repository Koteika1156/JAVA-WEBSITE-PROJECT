package com.example.demo.util;

import com.example.demo.models.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class TokenUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.access_lifetime}")
    private long ACCESS_TOKEN_EXPIRATION;
    @Value("${jwt.refresh_lifetime}")
    private long REFRESH_TOKEN_EXPIRATION;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private static final String SUB_KEY = "sub";
    private static final String EXP_KEY = "exp";
    private static final String IAT_KEY = "iat";
    private static final String ROLE_KEY = "role";

    public String generateAccessToken(String userId, Roles role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claim(SUB_KEY, userId)
                .claim(ROLE_KEY, role)
                .claim(IAT_KEY, Date.from(now))
                .claim(EXP_KEY, Date.from(now.plusMillis(ACCESS_TOKEN_EXPIRATION)))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claim(SUB_KEY, userId)
                .claim(IAT_KEY, Date.from(now))
                .claim(EXP_KEY, Date.from(now.plusMillis(REFRESH_TOKEN_EXPIRATION)))
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, String userId) {
        try {
            Claims claims = parseToken(token);
            return userId.equals(claims.get(SUB_KEY)) && !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.get(EXP_KEY, Date.class);
        return expiration.before(new Date());
    }

    public String getUserId(String token) {
        return getAllClaims(token).getSubject();
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
