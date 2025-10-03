package com.example.auth_service.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth_service.dto.LoginRequestDto;
import com.example.auth_service.dto.RegisterRequestDto;
import com.example.auth_service.dto.RegisterResponseDto;
import com.example.auth_service.entity.Token;
import com.example.auth_service.entity.User;
import com.example.auth_service.enums.TokenExpiryType;
import com.example.auth_service.exception.EmailAlreadyPresentException;
import com.example.auth_service.exception.InvalidCredentialsException;
import com.example.auth_service.exception.UserWithEmailDoesNotExistException;
import com.example.auth_service.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

	private UserRepository userRepository;
	private JwtUtilsService jwtUtilService;
	private PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, JwtUtilsService jwtUtilsService,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.jwtUtilService = jwtUtilsService;
		this.passwordEncoder = passwordEncoder;
	}

	public RegisterResponseDto registerUser(RegisterRequestDto requestDto) {

		Optional<User> existingUser = userRepository.findByEmail(requestDto.getEmail());

		if (existingUser.isPresent()) {
			throw new EmailAlreadyPresentException("user with the email id : " + requestDto.getEmail()
					+ " is already present exist, please login using that email");
		}

		final String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		User userToBeSaved = new User();
		userToBeSaved.setEmail(requestDto.getEmail());
		userToBeSaved.setUserName(requestDto.getUserName());

		Map<String, Object> claims = Map.of(
				"email", requestDto.getEmail(),
				"userName", requestDto.getUserName());

		String refreshTokenString = jwtUtilService.generateToken(claims, TokenExpiryType.REFRESH_TOKEN);

		userToBeSaved.setPassword(encodedPassword);

		Token token = new Token();
		token.setRefreshToken(refreshTokenString);
		token.setUser(userToBeSaved);
		userToBeSaved.setRefreshToken(token);

		User savedUser = userRepository.save(userToBeSaved);

		return new RegisterResponseDto(savedUser.getEmail(), savedUser.getUserName());
	}

	public String generateAccessTokenOnLogin(LoginRequestDto loginRequestDto) {
		// check if the password is correct
		Optional<User> userOptional = userRepository.findByEmail(loginRequestDto.getEmail());

		if (userOptional.isEmpty()) {
			throw new UserWithEmailDoesNotExistException(
					"the user with email : " + loginRequestDto.getEmail() + " does not exist");
		}

		if (!passwordEncoder.matches(loginRequestDto.getPassword(), userOptional.get().getPassword())) {
			throw new InvalidCredentialsException("the password is incorrect");
		}

		User user = userOptional.get();

		Map<String, Object> refreshTokenClaims = Map.of(
				"email", user.getEmail(),
				"userName", user.getUserName());

		// generate a new refresh token and save it with the user
		Token newRefreshToken = new Token();
		String newRefreshTokenString = jwtUtilService.generateToken(refreshTokenClaims, TokenExpiryType.REFRESH_TOKEN);

		newRefreshToken.setRefreshToken(newRefreshTokenString);
		newRefreshToken.setUser(user);
		user.setRefreshToken(newRefreshToken);

		userRepository.save(user);

		List<String> roles = List.of("USER");
		if (user.getIsAdmin()) {
			roles.add("ADMIN");
		}

		Map<String, Object> claims = Map.of(
				"email", user.getEmail(),
				"userName", user.getUserName(),
				"userId", user.getUserId().toString(),
				"roles", roles);

		String accessToken = jwtUtilService.generateToken(claims, TokenExpiryType.ACCESS_TOKEN);

		return accessToken;
	}
}
