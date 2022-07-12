package com.project.mungfriend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Json형태로 restApiException Body 부분에 태워 보내준다.
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class})
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException();

        ErrorCode errorCode = ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION;

        restApiException.setHttpStatus(errorCode.getHttpStatus());
        restApiException.setErrorMessage(ex.getMessage());

        return new ResponseEntity(restApiException, restApiException.getHttpStatus());
    }

    @ExceptionHandler(value = { NullPointerException.class})
    public ResponseEntity<Object> handleApiRequestException(NullPointerException ex) {
        RestApiException restApiException = new RestApiException();

        ErrorCode errorCode = ErrorCode.NULL_POINER_EXCEPTION;

        restApiException.setHttpStatus(errorCode.getHttpStatus());
        restApiException.setErrorMessage(ex.getMessage());

        return new ResponseEntity(restApiException, restApiException.getHttpStatus());
    }
}