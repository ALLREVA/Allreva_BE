package com.backend.allreva.chatting.chat_room.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreviewMessage {

    private long previewMessageNumber;
    private String previewText;

    private LocalDateTime sentAt;

    @Builder
    public PreviewMessage(
            final long previewMessageNumber,
            final String previewText,
            final LocalDateTime sentAt
    ) {
        this.previewMessageNumber = previewMessageNumber;
        this.previewText = previewText;
        this.sentAt = sentAt;
    }
}
