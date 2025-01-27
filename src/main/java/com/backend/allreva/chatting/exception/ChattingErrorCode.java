package com.backend.allreva.chatting.exception;

import com.backend.allreva.common.exception.code.ErrorCode;
import com.backend.allreva.common.exception.code.ErrorCodeInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ChattingErrorCode implements ErrorCodeInterface {

    TITLE_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "TITLE_TOO_LONG", "채팅방 제목의 길이는 20자 이하여야 합니다."),
    DESCRIPTION_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "DESCRIPTION_TOO_LONG", "채팅방 소개의 길이는 50자 이하여야 합니다."),
    GROUP_CHAT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "GROUP_CHAT_NOT_FOUND", "해당 단체 채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "CHAT_ROOM_NOT_FOUND", "해당 채팅방을 찾을 수 없습니다."),
    INVALID_MANAGER(HttpStatus.FORBIDDEN.value(), "INVALID_MANAGER", "채팅방 수정 권한이 없습니다."),
    DO_NOT_MEET_CONDITIONS_TO_DELETE(HttpStatus.BAD_REQUEST.value(), "DO_NOT_MEET_CONDITION_TO_DELETE", "방장 혼자 남은 경우만 삭제가 가능합니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.of(status,errorCode,message);
    }
}
