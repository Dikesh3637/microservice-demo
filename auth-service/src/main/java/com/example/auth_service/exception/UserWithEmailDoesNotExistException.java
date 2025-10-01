
package com.example.auth_service.exception;

public class UserWithEmailDoesNotExistException extends RuntimeException {

	public UserWithEmailDoesNotExistException(String message) {
		super(message);
	}

}
