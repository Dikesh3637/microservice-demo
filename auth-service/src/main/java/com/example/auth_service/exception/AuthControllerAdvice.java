
package com.example.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.auth_service.dto.ErrorResponseDto;

@ControllerAdvice
public class AuthControllerAdvice {

	@ExceptionHandler(EmailAlreadyPresentException.class)
	public ResponseEntity<ErrorResponseDto> EmailAlreadyPresentExceptionHandler(EmailAlreadyPresentException ex) {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setStatus(HttpStatus.BAD_REQUEST);
		errorResponseDto.setMessage(ex.getMessage());
		errorResponseDto.setTimestamp(System.currentTimeMillis());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
	}

	@ExceptionHandler(UserWithEmailDoesNotExistException.class)
	public ResponseEntity<ErrorResponseDto> UserWithEmailDoesNotExistExceptionHandler(
			UserWithEmailDoesNotExistException ex) {

		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setStatus(HttpStatus.BAD_REQUEST);
		errorResponseDto.setMessage(ex.getMessage());
		errorResponseDto.setTimestamp(System.currentTimeMillis());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
	}

	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<ErrorResponseDto> InvalidRefreshTokenExceptionHandler(UserWithEmailDoesNotExistException ex) {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setStatus(HttpStatus.BAD_REQUEST);
		errorResponseDto.setMessage(ex.getMessage());
		errorResponseDto.setTimestamp(System.currentTimeMillis());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
	}

}
