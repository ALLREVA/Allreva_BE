package com.backend.allreva.chatting.chat.integration.model.value;

import com.backend.allreva.common.model.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatInfoSummary {

    private String title;
    private Image thumbnail;

    private int headcount;

    public ChatInfoSummary(
            final String title,
            final Image thumbnail,
            final int headcount
    ) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.headcount = headcount;
    }

    public ChatInfoSummary(
            final String title,
            final Image thumbnail
    ) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.headcount = 2;
    }
}
