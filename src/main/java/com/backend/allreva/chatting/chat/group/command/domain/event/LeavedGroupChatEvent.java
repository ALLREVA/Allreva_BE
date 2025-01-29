package com.backend.allreva.chatting.chat.group.command.domain.event;

import com.backend.allreva.common.event.Event;
import lombok.Getter;

@Getter
public class LeavedGroupChatEvent extends Event {

    private final Long memberId;
    private final Long groupChatId;

    public LeavedGroupChatEvent(
            final Long memberId,
            final Long groupChatId
    ) {
        this.memberId = memberId;
        this.groupChatId = groupChatId;
    }
}
