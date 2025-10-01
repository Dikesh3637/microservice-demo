package com.example.auth_service.exception;

public class EmailAlreadyPresentException extends RuntimeException {
	public EmailAlreadyPresentException(String message) {
		super(message);
	}
}
