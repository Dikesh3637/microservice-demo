package com.example.auth_service.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.auth_service.enums.TokenExpiryType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtilsService {

	Integer refreshTokenExpiryTime = 1000 * 60 * 60 * 24 * 2;
	Integer accessTokenExpiryTime = 1000 * 60 * 15;

	Map<TokenExpiryType, Integer> tokenExpiryTimeMap = Map.of(
			TokenExpiryType.REFRESH_TOKEN, refreshTokenExpiryTime,
			TokenExpiryType.ACCESS_TOKEN, accessTokenExpiryTime);

	@Value("${JWT_SECRET:7044bc1392e8cb46df12cfbf848f3adcb2a5e8dd9600ada476f3ad91ba3d9895}")
	private String jwtSecretString;

	private SecretKey generateSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(Map<String, String> claims, TokenExpiryType expiryType) {
		Date now = new Date();
		Date exp = new Date(System.currentTimeMillis() + tokenExpiryTimeMap.get(expiryType));

		String refreshToken = Jwts.builder()
				.claims(claims)
				.issuedAt(now)
				.expiration(exp)
				.signWith(generateSecretKey())
				.compact();

		return refreshToken;
	}

	public void validateToken(String refreshToken) {
		Jwts.parser()
				.verifyWith(generateSecretKey())
				.build()
				.parseSignedClaims(refreshToken);
	}

	public boolean isTokenExpired(String token) {
		try {
			validateToken(token);
			return false;
		} catch (ExpiredJwtException ex) {
			return true;
		}
	}

	public String extractClaim(String token, String claimName) {
		Claims claims = Jwts.parser()
				.build()
				.parseUnsecuredClaims(token)
				.getPayload();

		String claim = (String) claims.getOrDefault(claimName, null);

		return claim;
	}

	public String refreshAccessToken(String accessToken) {
		String email = extractClaim(accessToken, "email");
		String userId = extractClaim(accessToken, "userId");
		String userName = extractClaim(accessToken, "userName");

		Map<String, String> claims = Map.of(
				"email", email,
				"userId", userId,
				"userName", userName);

		return generateToken(claims, TokenExpiryType.ACCESS_TOKEN);
	}

}
