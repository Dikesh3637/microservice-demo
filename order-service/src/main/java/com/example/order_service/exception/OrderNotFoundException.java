package com.example.order_service.exception;

/**
 * OrderNotFoundException
 */
public class OrderNotFoundException extends RuntimeException {

	public OrderNotFoundException(String message) {
		super(message);
	}
}
