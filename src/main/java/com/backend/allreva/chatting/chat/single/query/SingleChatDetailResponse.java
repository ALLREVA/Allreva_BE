package com.backend.allreva.chatting.chat.single.query;

import com.backend.allreva.chatting.chat.integration.model.value.Participant;
import com.backend.allreva.common.model.Image;
import lombok.Getter;

@Getter
public class SingleChatDetailResponse {

    private final Image thumbnail;
    private final String title;

    private final Participant me;
    private final Participant otherMember;

    public SingleChatDetailResponse(
            final Image thumbnail,
            final String title,
            final Participant me,
            final Participant otherMember
    ) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.me = me;
        this.otherMember = otherMember;
    }
}
