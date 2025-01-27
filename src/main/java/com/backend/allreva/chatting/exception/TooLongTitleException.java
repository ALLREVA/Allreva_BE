package com.backend.allreva.chatting.exception;

import com.backend.allreva.common.exception.CustomException;

public class TooLongTitleException extends CustomException {

    public TooLongTitleException() {
        super(ChattingErrorCode.TITLE_TOO_LONG);
    }
}
