package com.example.dailyLog.exception.commonException.error;

import com.example.dailyLog.exception.commonException.CommonErrorCode;

public class DuplicateEmailException extends BizException {
    public DuplicateEmailException() {

      super(CommonErrorCode.DUPLICATE_EMAIL);
    }
}
