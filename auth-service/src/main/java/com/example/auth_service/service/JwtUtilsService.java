package com.example.auth_service.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtilsService {

	@Value("${JWT_SECRET:7044bc1392e8cb46df12cfbf848f3adcb2a5e8dd9600ada476f3ad91ba3d9895}")
	private String jwtSecretString;

	private SecretKey generateSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
	}

	public String generateRefreshToken(Map<String, String> claims) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + 1000 * 60 * 60 * 24 * 2);
		String refreshToken = Jwts.builder()
				.claims(claims)
				.issuedAt(now)
				.expiration(exp)
				.signWith(generateSecretKey())
				.compact();

		return refreshToken;
	}

}
