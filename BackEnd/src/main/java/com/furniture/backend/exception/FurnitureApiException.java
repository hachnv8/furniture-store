package com.furniture.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FurnitureApiException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -6593330219878485669L;

	private final HttpStatus status;
	private final String message;

	public FurnitureApiException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public FurnitureApiException(HttpStatus status, String message, Throwable exception) {
		super(exception);
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
