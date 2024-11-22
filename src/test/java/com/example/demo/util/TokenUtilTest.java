package com.example.demo.util;


import com.example.demo.models.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = TokenUtil.class)
public class TokenUtilTest {

    @Autowired
    private TokenUtil tokenUtil;

    private SecretKey invalidKey;

    private final String testUserId = "testUser";
    private final UserRole testUserRole = UserRole.USER;

    @BeforeEach
    void setUp() {
        invalidKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    @Test
    void valuesLoads() {
        assertNotNull(tokenUtil.getSECRET_KEY());
        assertNotEquals(0, tokenUtil.getACCESS_TOKEN_EXPIRATION());

        assertNotEquals(0, tokenUtil.getREFRESH_TOKEN_EXPIRATION());
    }

    @Test
    void parseToken_verifyingWithValidKey() {
        String accessToken = tokenUtil.generateAccessToken(testUserId, testUserRole);

        Claims claims = tokenUtil.parseToken(accessToken);
        Claims rightClaims = Jwts.parser()
                .verifyWith(tokenUtil.getKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        assertEquals(rightClaims, claims);
    }

    @Test
    void testTokenGeneratedAccessWithValidKey() {
        String accessToken = tokenUtil.generateAccessToken(testUserId, testUserRole);
        assertNotNull(accessToken, "Токен не должен быть null");

        Claims claims = Jwts.parser()
                .verifyWith(tokenUtil.getKey())
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        assertEquals(testUserId, claims.get(TokenUtil.SUB_KEY));
        assertEquals(testUserRole.name(), claims.get(TokenUtil.ROLE_KEY));
        assertNotNull(claims.get(TokenUtil.EXP_KEY));
    }

    @Test
    void testTokenGeneratedAccessWithInvalidKey() {
        String accessToken = tokenUtil.generateAccessToken(testUserId, testUserRole);

        assertThrows(
                io.jsonwebtoken.security.WeakKeyException.class,
                () -> {
                    Jwts.parser()
                            .verifyWith(invalidKey)
                            .build()
                            .parseClaimsJws(accessToken)
                            .getBody();
                }
        );
    }

    @Test
    void testTokenGeneratedRefreshWithValidKey() {
        String refreshToken = tokenUtil.generateRefreshToken(testUserId);
        assertNotNull(refreshToken, "Токен не должен быть null");

        Claims claims = Jwts.parser()
                .verifyWith(tokenUtil.getKey())
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        assertEquals(testUserId, claims.get(TokenUtil.SUB_KEY));
        assertNotNull(claims.get(TokenUtil.EXP_KEY));
    }

    @Test
    void testTokenGeneratedRefreshWithInvalidKey() {
        String refreshToken = tokenUtil.generateRefreshToken(testUserId);

        assertThrows(
                io.jsonwebtoken.security.WeakKeyException.class,
                () -> {
                    Jwts.parser()
                            .verifyWith(invalidKey)
                            .build()
                            .parseClaimsJws(refreshToken)
                            .getBody();
                }
        );
    }

    @Test
    void isValidGeneratedTokens() {
        String accessToken = tokenUtil.generateAccessToken(testUserId, testUserRole);
        String refreshToken = tokenUtil.generateRefreshToken(testUserId);


        assertTrue(tokenUtil.isTokenValid(accessToken));
        assertTrue(tokenUtil.isTokenValid(refreshToken));
    }

    @Test
    void isValid_onInvalidSign() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG" +
                "9lIiwiaWF0IjoxOTE2MjM5MDIyfQ.DGme0_qbXW79j7VftKA1L1b6qugxEJ6-nqcmnvmi7DA";

        assertFalse(tokenUtil.isTokenValid(invalidToken));
    }

    @Test
    void isValid_onInvalidExpired() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4g" +
                "RG9lIiwiaWF0IjoxMzE2MjM5MDIyfQ.SXOAGI1a5jMWqvfMTgBGBhBdDOalKiWwIX5o0UMEZqw";

        assertFalse(tokenUtil.isTokenValid(invalidToken));
    }

    @Test
    void getUserIdOnAccessToken_justWorks() {
        String accessToken = tokenUtil.generateAccessToken(testUserId, testUserRole);

        String userId = tokenUtil.getUserId(accessToken);

        assertNotNull(userId);
        assertEquals(testUserId, userId);
    }

    @Test
    void getUserIdOnRefreshToken_justWorks() {
        String refreshToken = tokenUtil.generateRefreshToken(testUserId);

        String userId = tokenUtil.getUserId(refreshToken);

        assertNotNull(userId);
        assertEquals(testUserId, userId);
    }

    @Test
    void getUserRole_justWorks() {
        String accessToken = tokenUtil.generateAccessToken(testUserId, testUserRole);

        UserRole userRole = tokenUtil.getUserRole(accessToken);

        assertNotNull(userRole);
        assertEquals(testUserRole, userRole);
    }
}
