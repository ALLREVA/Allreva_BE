package com.backend.allreva.chatting.chat.group.command.domain.event;

import com.backend.allreva.common.event.Event;
import com.backend.allreva.common.model.Image;
import lombok.Getter;

@Getter
public class UpdatedGroupChatEvent extends Event {

    private final Long memberId;
    private final Long groupChatId;
    private final String title;
    private final Image thumbnail;

    public UpdatedGroupChatEvent(
            final Long memberId,
            final Long groupChatId,
            final String title,
            final Image thumbnail
    ) {
        this.memberId = memberId;
        this.groupChatId = groupChatId;
        this.title = title;
        this.thumbnail = thumbnail;
    }
}
