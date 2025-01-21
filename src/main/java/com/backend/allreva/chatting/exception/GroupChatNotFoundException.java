package com.backend.allreva.chatting.exception;

import com.backend.allreva.common.exception.CustomException;

public class GroupChatNotFoundException extends CustomException {


    public GroupChatNotFoundException() {
        super(ChattingErrorCode.GROUP_CHAT_NOT_FOUND);
    }
}
