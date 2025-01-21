package com.backend.allreva.chatting.chat_room.model;

import com.backend.allreva.common.model.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomInfo {

    private String title;
    private Image thumbnail;

    private int headcount;

    public RoomInfo(
            final String title,
            final Image thumbnail,
            final int headcount
    ) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.headcount = headcount;
    }

    public RoomInfo(
            final String title,
            final Image thumbnail
    ) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.headcount = 2;
    }
}
