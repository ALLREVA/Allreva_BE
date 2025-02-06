package com.backend.allreva.chatting.chat.single.command.domain.event;

import com.backend.allreva.common.event.Event;
import lombok.Getter;

@Getter
public class AddedSingleChatEvent extends Event {

    private final Long singleChatId;
    private final Long memberId;
    private final Long otherMemberId;

    public AddedSingleChatEvent(
            final StartedSingleChatEvent event
    ) {
        this.singleChatId = event.getSingleChatId();
        this.memberId = event.getMemberId();
        this.otherMemberId = event.getOtherMemberId();
    }


    public AddedSingleChatEvent(
            final Long singleChatId,
            final Long memberId,
            final Long otherMemberId
    ) {
        this.singleChatId = singleChatId;
        this.memberId = memberId;
        this.otherMemberId = otherMemberId;
    }
}
