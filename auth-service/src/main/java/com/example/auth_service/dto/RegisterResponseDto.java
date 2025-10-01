package com.example.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterResponseDto {

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String userName;

	public RegisterResponseDto() {

	}

	public RegisterResponseDto(String email, String userName) {
		this.email = email;
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
