package com.project.mungfriend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "400_1"),
    NULL_POINER_EXCEPTION(HttpStatus.BAD_REQUEST, "400_2");

    private final HttpStatus httpStatus;
    private final String errorCode;

    ErrorCode(HttpStatus httpStatus, String errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}