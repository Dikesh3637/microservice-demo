package com.example.auth_service.service;

import java.util.Map;
import java.util.Optional;

import org.example.auth.grpc.AuthServiceGrpc.AuthServiceImplBase;
import org.example.auth.grpc.ValidationRequest;
import org.example.auth.grpc.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.auth_service.entity.Token;
import com.example.auth_service.entity.User;
import com.example.auth_service.enums.TokenExpiryType;
import com.example.auth_service.repository.UserRepository;

import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AuthGrpcService extends AuthServiceImplBase {

	private final JwtUtilsService jwtUtilsService;
	private final UserRepository userRepository;
	private final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

	public AuthGrpcService(JwtUtilsService jwtUtilsService, UserRepository userRepository) {
		this.jwtUtilsService = jwtUtilsService;
		this.userRepository = userRepository;
	}

	@Override
	public void validateToken(ValidationRequest request, StreamObserver<ValidationResponse> responseObserver) {
		String accessToken = request.getAccessToken();

		// Handle null or empty token
		if (accessToken == null || accessToken.trim().isEmpty()) {
			logger.warn("Received null or empty access token");
			ValidationResponse validationResponse = ValidationResponse.newBuilder()
					.setIsValid(false)
					.setIsRefreshed(false)
					.build();
			sendValidateTokenResponse(responseObserver, validationResponse);
			return;
		}

		accessToken = accessToken.trim();

		try {
			jwtUtilsService.validateToken(accessToken);

			// Token is valid, send success response
			ValidationResponse validationResponse = ValidationResponse.newBuilder()
					.setIsValid(true)
					.setIsRefreshed(false)
					.build();
			sendValidateTokenResponse(responseObserver, validationResponse);

		} catch (SignatureException ex) {
			logger.warn("Invalid token signature");
			ValidationResponse validationResponse = ValidationResponse.newBuilder()
					.setIsValid(false)
					.setIsRefreshed(false)
					.build();
			sendValidateTokenResponse(responseObserver, validationResponse);

		} catch (ExpiredJwtException ex) {
			logger.info("Token expired, attempting refresh");
			handleExpiredToken(ex, responseObserver);

		} catch (JwtException ex) {
			logger.error("JWT validation error: {}", ex.getMessage());
			ValidationResponse validationResponse = ValidationResponse.newBuilder()
					.setIsValid(false)
					.setIsRefreshed(false)
					.build();
			sendValidateTokenResponse(responseObserver, validationResponse);

		} catch (Exception ex) {
			logger.error("Unexpected error during token validation", ex);
			responseObserver.onError(ex);
		}
	}

	private void handleExpiredToken(ExpiredJwtException ex, StreamObserver<ValidationResponse> responseObserver) {
		try {
			Claims claims = ex.getClaims();

			String email = claims.get("email", String.class);

			if (email == null || email.isEmpty()) {
				logger.warn("Email claim missing from expired token");
				ValidationResponse validationResponse = ValidationResponse.newBuilder()
						.setIsValid(false)
						.setIsRefreshed(false)
						.build();
				sendValidateTokenResponse(responseObserver, validationResponse);
				return; // CRITICAL: Must return here
			}

			Optional<User> userOptional = userRepository.findByEmail(email);

			if (userOptional.isEmpty()) {
				logger.warn("User not found for email: {}", email);
				ValidationResponse validationResponse = ValidationResponse.newBuilder()
						.setIsValid(false)
						.setIsRefreshed(false)
						.build();
				sendValidateTokenResponse(responseObserver, validationResponse);
				return;
			}

			User user = userOptional.get();
			Token refreshToken = user.getRefreshToken();

			if (refreshToken == null || refreshToken.getRefreshToken() == null) {
				logger.warn("No refresh token found for user: {}", email);
				ValidationResponse validationResponse = ValidationResponse.newBuilder()
						.setIsValid(false)
						.setIsRefreshed(false)
						.build();
				sendValidateTokenResponse(responseObserver, validationResponse);
				return;
			}

			String refreshTokenString = refreshToken.getRefreshToken();

			try {
				jwtUtilsService.validateToken(refreshTokenString);
			} catch (JwtException exception) {
				logger.warn("Refresh token validation failed for user: {}", email);
				ValidationResponse validationResponse = ValidationResponse.newBuilder()
						.setIsValid(false)
						.setIsRefreshed(false)
						.build();
				sendValidateTokenResponse(responseObserver, validationResponse);
				return;
			}

			Map<String, String> newAccessTokenClaims = Map.of(
					"email", user.getEmail(),
					"userId", user.getUserId().toString(),
					"userName", user.getUserName());

			String refreshedAccessToken = jwtUtilsService.generateToken(newAccessTokenClaims,
					TokenExpiryType.ACCESS_TOKEN);

			logger.info("Successfully refreshed access token for user: {}", email);
			ValidationResponse validationResponse = ValidationResponse.newBuilder()
					.setIsValid(true)
					.setIsRefreshed(true)
					.setRefreshedAccessToken(refreshedAccessToken)
					.build();

			sendValidateTokenResponse(responseObserver, validationResponse);

		} catch (Exception exception) {
			logger.error("Error while handling expired token", exception);
			responseObserver.onError(exception);
		}
	}

	private void sendValidateTokenResponse(StreamObserver<ValidationResponse> responseObserver,
			ValidationResponse validationResponse) {
		responseObserver.onNext(validationResponse);
		responseObserver.onCompleted();
	}
}
