package com.backend.allreva.chatting.notification;

import com.backend.allreva.common.event.Event;
import lombok.Getter;

@Getter
public class TimedOutEvent extends Event {

    private final Long memberId;

    public TimedOutEvent(final Long memberId) {
        this.memberId = memberId;
    }
}
