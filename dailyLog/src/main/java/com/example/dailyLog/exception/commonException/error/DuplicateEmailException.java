package com.example.dailyLog.exception.commonException.error;

import com.example.dailyLog.exception.commonException.CommonErrorCode;
import com.example.dailyLog.exception.loginException.LoginErrorCode;

public class DuplicateEmailException {
    public DuplicateEmailException() {

      super(LoginErrorCode.DUPLICATE_EMAIL);
    }
}
