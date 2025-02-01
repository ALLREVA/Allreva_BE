package com.backend.allreva.chatting.message.query;

import lombok.Getter;

import java.util.List;

@Getter
public class EnterChatResponse {

    private final Long myId;
    private final List<MessageResponse> messages;

    public EnterChatResponse(
            final Long myId,
            final List<MessageResponse> messages
    ) {
        this.myId = myId;
        this.messages = messages;
    }
}
