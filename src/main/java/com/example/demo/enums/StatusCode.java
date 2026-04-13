package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StatusCode {
    SUCCESS("0000", "Success", HttpStatus.OK),
    BAD_REQUEST("1000", "Invalid request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("1001", "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("1003", "Access denied", HttpStatus.FORBIDDEN),
    NOT_FOUND("1004", "Data not found", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR("1005", "Invalid data", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("2000", "System error", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("2001", "Service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String messenge;
    private final HttpStatus httpStatus;

}
