package com.example.demo.util;

import com.example.demo.models.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Getter
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

    public static final String SUB_KEY = "sub";
    public static final String EXP_KEY = "exp";
    public static final String IAT_KEY = "iat";
    public static final String ROLE_KEY = "role";

    public String generateAccessToken(String userId, UserRole role) {
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

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseToken(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.get(EXP_KEY, Date.class);
        return expiration.before(new Date());
    }

    public String getUserId(String token) {
        return parseToken(token).getSubject();
    }

    public UserRole getUserRole(String token) {
        String roleString = (String) parseToken(token).get(ROLE_KEY);
        return UserRole.fromValue(roleString);
    }
}

