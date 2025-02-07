package com.backend.allreva.chatting.chat.integration.model;

import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.common.event.Event;
import lombok.Getter;

@Getter
public class EnteredChatEvent extends Event {

    private final Long chatId;
    private final ChatType chatType;

    private final Long memberId;

    private final Long lastReadMessageNumber;

    public EnteredChatEvent(
            final Long chatId,
            final ChatType chatType,
            final Long memberId,
            final Long lastReadMessageNumber
    ) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.memberId = memberId;
        this.lastReadMessageNumber = lastReadMessageNumber;
    }
}
