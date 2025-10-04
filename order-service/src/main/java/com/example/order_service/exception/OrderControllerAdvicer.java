package com.example.order_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.order_service.dto.ErrorResponseDto;

/**
 * OrderControllerAdvicer
 */
@RestControllerAdvice
public class OrderControllerAdvicer {

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleOrderNotFoundException(OrderNotFoundException e) {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND, e.getMessage(),
				System.currentTimeMillis());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
	}
}
