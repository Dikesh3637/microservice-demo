package com.example.auth_service.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_service.dto.LoginRequestDto;
import com.example.auth_service.dto.RegisterRequestDto;
import com.example.auth_service.dto.RegisterResponseDto;
import com.example.auth_service.service.AuthService;

@RestController
public class AuthController {

	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDto> registerUser(@RequestBody RegisterRequestDto requestDto) {
		RegisterResponseDto response = authService.registerUser(requestDto);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<Void> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
		String accessToken = authService.generateAccessTokenOnLogin(loginRequestDto);
		ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.sameSite("Lax")
				.build();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();

	}

}
