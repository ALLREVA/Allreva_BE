package com.backend.allreva.chatting.chat.group_chat.command.domain;

import com.backend.allreva.common.event.Event;
import lombok.Getter;

@Getter
public class DeletedGroupChatEvent extends Event {

    private final Long groupChatId;
    private final Long memberId;

    public DeletedGroupChatEvent(
            final Long groupChatId,
            final Long memberId
    ) {
        this.groupChatId = groupChatId;
        this.memberId = memberId;
    }
}
