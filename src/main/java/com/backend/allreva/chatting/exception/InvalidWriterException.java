package com.backend.allreva.chatting.exception;

import com.backend.allreva.common.exception.CustomException;

public class InvalidWriterException extends CustomException {

    public InvalidWriterException() {
        super(ChattingErrorCode.INVALID_MANAGER);
    }
}
