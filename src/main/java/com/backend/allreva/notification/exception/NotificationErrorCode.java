package com.backend.allreva.notification.exception;

import com.backend.allreva.common.exception.code.ErrorCode;
import com.backend.allreva.common.exception.code.ErrorCodeInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum NotificationErrorCode implements ErrorCodeInterface {

    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "NOTIFICATION_NOT_FOUND", "존재하지 않는 알람입니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.of(status, errorCode, message);
    }
}
