package com.backend.allreva.chatting.chat.single.command.application.dto;

import lombok.Getter;

@Getter
public class StartSingleChattingResponse {

    private final Long singleChatId;
    private final Long lastReadMessageNumber;

    public StartSingleChattingResponse(final Long singleChatId) {
        this.singleChatId = singleChatId;
        this.lastReadMessageNumber = 0L;
    }

    public StartSingleChattingResponse(
            final Long singleChatId,
            final Long lastReadMessageNumber
    ) {
        this.singleChatId = singleChatId;
        this.lastReadMessageNumber = lastReadMessageNumber;
    }
}
