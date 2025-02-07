package com.backend.allreva.chatting.message.query;

import lombok.Getter;

import java.util.List;

@Getter
public class EnterChatResponse {

    private final Long myId;
    private final Long lastReadMessageNumber;
    private final List<MessageResponse> messages;

    public EnterChatResponse(
            final Long myId,
            final Long lastReadMessageNumber,
            final List<MessageResponse> messages
    ) {
        this.myId = myId;
        this.lastReadMessageNumber = lastReadMessageNumber;
        this.messages = messages;
    }
}
