package com.backend.allreva.chatting.chat.single.command.domain.event;

import com.backend.allreva.common.event.Event;
import lombok.Getter;

@Getter
public class LeavedSingleChatEvent extends Event {

    private final Long memberId;
    private final Long singleChatId;

    public LeavedSingleChatEvent(
            final Long memberId,
            final Long singleChatId
    ) {
        this.memberId = memberId;
        this.singleChatId = singleChatId;
    }
}
