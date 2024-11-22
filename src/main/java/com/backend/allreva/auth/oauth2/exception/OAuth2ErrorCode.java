package com.backend.allreva.auth.oauth2.exception;

import org.springframework.http.HttpStatus;

import com.backend.allreva.common.exception.code.ErrorCode;
import com.backend.allreva.common.exception.code.ErrorCodeInterface;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuth2ErrorCode implements ErrorCodeInterface {

    UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST.value(), "UNSUPPORTED_PROVIDER", "지원하지 않는 provider 입니다.")
    ;

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.of(status, errorCode, message);
    }
}