package com.backend.allreva.member.command.domain;

import com.backend.allreva.common.event.Event;
import lombok.Getter;

@Getter
public class AddedMemberEvent extends Event {

    private final Long memberId;

    public AddedMemberEvent(Long memberId) {
        this.memberId = memberId;
    }
}
