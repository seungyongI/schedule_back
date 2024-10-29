package com.example.dailyLog.exception.imageException;

import com.example.dailyLog.exception.commonException.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ImageErrorCode implements ErrorCode {

    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 파일 크기가 허용된 최대 크기를 초과했습니다."),
    UNSUPPORTED_FILE_FORMAT(HttpStatus.BAD_REQUEST, "지원되지 않는 이미지 형식입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    FILE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일 저장 중 오류가 발생했습니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "업로드할 이미지 파일이 비어 있습니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "잘못된 파일 이름입니다. 파일 이름에는 특수 문자가 포함될 수 없습니다.");

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
