package com.backend.allreva.hall.exception;

import com.backend.allreva.common.exception.code.ErrorCode;
import com.backend.allreva.common.exception.code.ErrorCodeInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum ConcertHallErrorCode implements ErrorCodeInterface {
    CONCERT_HALL_SEARCH_NOTFOUND(HttpStatus.NOT_FOUND.value(), "CONCERT_HALL_SEARCH_NOT_FOUND", "존재하는 공연장이 없습니다"),
    RELATED_CONCERT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "RELATED_CONCERT_EXCEPTION", "연관 콘서트 에러");
    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.of(status, errorCode, message);
    }
}
