package com.furniture.backend.exception;

import com.furniture.backend.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Created by HachNV on Mar 10, 2023
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadCredentialsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    private ApiResponse apiResponse;

    public BadCredentialsException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}
