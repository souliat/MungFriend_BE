package com.project.mungfriend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "400_1"),
    NULL_POINTER_EXCEPTION(HttpStatus.BAD_REQUEST, "400_2"),

    // request에 사진이 없이 왔을 경우 Required request part 'image' is not present 를 막아줌
    MISSING_SERVLET_REQUEST_PART_EXCEPTION(HttpStatus.BAD_REQUEST, "400_3"),

    CONSTRAINT_VIOLATION_EXCEPTION(HttpStatus.BAD_REQUEST, "400_4"),

    PROPERTY_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "400_5"),
    SIZE_LIMIT_EXCEEDED_EXCEPTION(HttpStatus.BAD_REQUEST, "400_6"),

    // 401 Unauthorized
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "401"),

    // 403 Forbidden
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN,"403");

    private final HttpStatus httpStatus;
    private final String errorCode;

    ErrorCode(HttpStatus httpStatus, String errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}