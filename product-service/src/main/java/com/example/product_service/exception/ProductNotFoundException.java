package com.example.product_service.exception;

/**
 * ProductNotFoundException
 */
public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(String message) {
		super(message);
	}
}
