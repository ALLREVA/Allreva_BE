package com.backend.allreva.common.application.exception;

import com.backend.allreva.common.exception.code.ErrorCode;
import com.backend.allreva.common.exception.code.ErrorCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCodeInterface {
    INVALID_URL(HttpStatus.NOT_FOUND.value(), "INVALID_URL", "유효하지 않은 URL 입니다."),
    FAIL_TO_MAKE_URL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "FAIL_TO_MAKE_URL", "URL 생성실패")
    ;
    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.of(status, errorCode, message);
    }
}
