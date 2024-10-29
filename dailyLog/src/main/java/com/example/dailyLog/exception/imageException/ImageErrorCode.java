package com.example.dailyLog.exception.imageException;

import com.example.dailyLog.exception.commonException.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ImageErrorCode implements ErrorCode {



    private final HttpStatus httpStatus;
    private final String message;

    ImageErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {

        return message;
    }
}
