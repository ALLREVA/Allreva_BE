package com.backend.allreva.chatting.chat.group.command.domain.event;

import com.backend.allreva.common.event.Event;
import lombok.Getter;

@Getter
public class AddedGroupChatEvent extends Event {

    private Long groupChatId;
    private Long memberId;

    public AddedGroupChatEvent(
            final Long groupChatId,
            final Long memberId
    ) {
        this.groupChatId = groupChatId;
        this.memberId = memberId;
    }
}
