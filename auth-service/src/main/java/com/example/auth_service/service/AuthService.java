package com.example.auth_service.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth_service.dto.LoginRequestDto;
import com.example.auth_service.dto.RegisterRequestDto;
import com.example.auth_service.dto.RegisterResponseDto;
import com.example.auth_service.entity.Token;
import com.example.auth_service.entity.User;
import com.example.auth_service.exception.EmailAlreadyPresentException;
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

		Map<String, String> claims = Map.of(
				"email", requestDto.getEmail(),
				"userName", requestDto.getUserName());

		String refreshTokenString = jwtUtilService.generateRefreshToken(claims);

		userToBeSaved.setPassword(encodedPassword);

		Token token = new Token();
		token.setRefreshToken(refreshTokenString);
		token.setUser(userToBeSaved);
		userToBeSaved.setRefreshToken(token);

		User savedUser = userRepository.save(userToBeSaved);

		return new RegisterResponseDto(savedUser.getEmail(), savedUser.getUserName());
	}

	public String generateAccessToken(LoginRequestDto loginRequestDto) {
		// check if the password is correct
		Optional<User> userOptional = userRepository.findByEmail(loginRequestDto.getEmail());
		if (userOptional.isEmpty()) {
			throw new UserWithEmailDoesNotExistException(
					"the user with email : " + loginRequestDto.getEmail() + " does not exist");
		}

		User user = userOptional.get();

	}
}
