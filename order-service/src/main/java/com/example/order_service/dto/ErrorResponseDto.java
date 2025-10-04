package com.example.order_service.dto;

import org.springframework.http.HttpStatusCode;

/**
 * ErrorResponseDto
 */
public class ErrorResponseDto {

	private HttpStatusCode status;
	private String message;
	private long timestamp;

	public ErrorResponseDto() {

	}

	public ErrorResponseDto(HttpStatusCode status, String message, long timestamp) {
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;

	}

	public HttpStatusCode getStatus() {
		return status;
	}

	public void setStatus(HttpStatusCode status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
