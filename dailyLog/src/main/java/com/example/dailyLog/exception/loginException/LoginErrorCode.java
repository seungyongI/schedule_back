package com.example.dailyLog.exception.loginException;

import com.example.dailyLog.exception.commonException.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum LoginErrorCode implements ErrorCode {

    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 Email이 존재하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Email 중복입니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "User_name 중복입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String message;

    LoginErrorCode(HttpStatus httpStatus, String message) {
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
