package com.backend.allreva.chatting.chat.query;

import com.backend.allreva.chatting.Participant;
import com.backend.allreva.common.model.Image;

import java.util.ArrayList;
import java.util.List;

public class RoomInfoDetail {

    private final Image thumbnail;
    private final String title;
    private final String description;
    private final List<Participant> participants;


    public RoomInfoDetail(
            final Image thumbnail,
            final String title,
            final List<Participant> participants
    ) {
        this(thumbnail, title, "", participants);
    }

    public RoomInfoDetail(
            final Image thumbnail,
            final String title,
            final String description,
            final List<Participant> participants
    ) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.participants = participants;
    }
}
