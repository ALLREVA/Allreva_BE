package com.backend.allreva.chatting.chat.query.model.chat_room;

import com.backend.allreva.common.model.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomInfoSummary {

    private String title;
    private Image thumbnail;

    private int headcount;

    public RoomInfoSummary(
            final String title,
            final Image thumbnail,
            final int headcount
    ) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.headcount = headcount;
    }

    public RoomInfoSummary(
            final String title,
            final Image thumbnail
    ) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.headcount = 2;
    }
}
