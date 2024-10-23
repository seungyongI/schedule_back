package com.example.dailyLog.exception;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;

public enum ErrorCode {

    NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 글이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,  "유저가 존재하지 않습니다."),;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {

        return message;
    }
}
