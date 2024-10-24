package com.example.dailyLog.exception.commonException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 공통 에러 응답 생성 메소드
    private ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .message(message)
                        .httpStatus(status)
                        .localDateTime(LocalDateTime.now())
                        .build());
    }


    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizException(BizException e){
        return createErrorResponse(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return createErrorResponse("Invalid request body", HttpStatus.BAD_REQUEST);
    }

    // 이메일 중복 처리
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleSqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return createErrorResponse("email 중복입니다.", HttpStatus.CONFLICT);
    }

}
