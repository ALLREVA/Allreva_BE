package com.backend.allreva.chatting.exception;

import com.backend.allreva.common.exception.CustomException;

public class TooLongDescriptionException extends CustomException {

    public TooLongDescriptionException() {
        super(ChattingErrorCode.DESCRIPTION_TOO_LONG);
    }

}
