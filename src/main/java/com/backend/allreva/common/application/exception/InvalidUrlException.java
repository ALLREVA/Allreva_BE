package com.backend.allreva.common.application.exception;

import com.backend.allreva.common.exception.CustomException;

import static com.backend.allreva.common.application.exception.FileErrorCode.INVALID_URL;

public class InvalidUrlException extends CustomException {
    public InvalidUrlException() {
        super(INVALID_URL);
    }
}
