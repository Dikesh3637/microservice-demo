package com.example.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDto {

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 3, max = 20)
	private String userName;

	@NotBlank
	private String password;

	public RegisterRequestDto() {

	}

	public RegisterRequestDto(String email, String userName, String password) {
		this.email = email;
		this.userName = userName;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
