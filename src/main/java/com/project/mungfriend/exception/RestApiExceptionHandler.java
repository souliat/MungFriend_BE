package com.project.mungfriend.exception;

import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;

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

        ErrorCode errorCode = ErrorCode.NULL_POINTER_EXCEPTION;

        restApiException.setHttpStatus(errorCode.getHttpStatus());
        restApiException.setErrorMessage(ex.getMessage());

        return new ResponseEntity(restApiException, restApiException.getHttpStatus());
    }

    @ExceptionHandler(value = { MissingServletRequestPartException.class})
    public ResponseEntity<Object> handleApiRequestException(MissingServletRequestPartException ex) {
        RestApiException restApiException = new RestApiException();

        ErrorCode errorCode = ErrorCode.MISSING_SERVLET_REQUEST_PART_EXCEPTION;

        restApiException.setHttpStatus(errorCode.getHttpStatus());
        restApiException.setErrorMessage("사진을 등록해주세요.");

        return new ResponseEntity(restApiException, restApiException.getHttpStatus());
    }

    @ExceptionHandler(value = { PropertyValueException.class})
    public ResponseEntity<Object> handleApiRequestException(PropertyValueException ex) {
        RestApiException restApiException = new RestApiException();

        ErrorCode errorCode = ErrorCode.PROPERTY_VALUE_EXCEPTION;

        restApiException.setHttpStatus(errorCode.getHttpStatus());
        restApiException.setErrorMessage("필수 정보를 모두 입력해주세요.");

        return new ResponseEntity(restApiException, restApiException.getHttpStatus());
    }


    @ExceptionHandler(value = { ConstraintViolationException.class})
    public ResponseEntity<Object> handleApiRequestException(ConstraintViolationException ex) {
        RestApiException restApiException = new RestApiException();

        ErrorCode errorCode = ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION;

        restApiException.setHttpStatus(errorCode.getHttpStatus());
        restApiException.setErrorMessage("필수 정보를 모두 입력해주세요.");

        return new ResponseEntity(restApiException, restApiException.getHttpStatus());
    }
}