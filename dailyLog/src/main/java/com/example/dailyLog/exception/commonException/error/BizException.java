package com.example.dailyLog.exception.commonException.error;

import com.example.dailyLog.exception.commonException.CommonErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BizException extends RuntimeException {

    private final CommonErrorCode commonErrorCode;

    public BizException(CommonErrorCode commonErrorCode) {
        // 예외 발생 시 메시지를 상위 클래스에 전달
        super(commonErrorCode.getMessage());
        this.commonErrorCode = commonErrorCode;
    }

    public HttpStatus getHttpStatus() {
        return commonErrorCode.getHttpStatus();
    }
}
