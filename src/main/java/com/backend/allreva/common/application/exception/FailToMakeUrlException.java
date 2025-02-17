package com.backend.allreva.common.application.exception;

import com.backend.allreva.common.exception.CustomException;

import static com.backend.allreva.common.application.exception.FileErrorCode.FAIL_TO_MAKE_URL;

public class FailToMakeUrlException extends CustomException {
    public FailToMakeUrlException() {
        super(FAIL_TO_MAKE_URL);
    }
}
