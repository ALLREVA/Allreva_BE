package com.backend.allreva.chatting.chat.group.query.response;

import com.backend.allreva.chatting.chat.integration.model.value.Participant;
import com.backend.allreva.common.model.Image;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupChatDetailResponse {

    private final Image thumbnail;
    private final String title;
    private final String description;

    private final Participant me;
    private final Participant manager;

    private final List<Participant> participants;


    public GroupChatDetailResponse(
            final Image thumbnail,
            final String title,
            final String description,
            final Participant me,
            final Participant manager,
            final List<Participant> participants
    ) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.me = me;
        this.manager = manager;
        this.participants = participants;
    }
}
