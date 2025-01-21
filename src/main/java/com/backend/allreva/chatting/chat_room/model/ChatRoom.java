package com.backend.allreva.chatting.chat_room.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"roomId", "roomType"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    private Long roomId;
    private RoomType roomType;

    private RoomInfo roomInfo; // title, thumbnail, headcount
    private PreviewMessage previewMessage;

    private long lastMessageNumber;

    public ChatRoom(
            final RoomInfo roomInfo,
            final PreviewMessage previewMessage,
            final long lastMessageNumber
    ) {
        this.roomInfo = roomInfo;
        this.previewMessage = previewMessage;
        this.lastMessageNumber = lastMessageNumber;
    }

    public void updatePreviewMessage(
            final long previewMessageNumber,
            final String previewMessage,
            final LocalDateTime sentAt
    ) {
        this.previewMessage = PreviewMessage.builder()
                .previewMessageNumber(previewMessageNumber)
                .previewText(previewMessage)
                .sentAt(sentAt)
                .build();
    }

    public void updatePreviewMessage(final PreviewMessage previewMessage) {
        this.previewMessage = previewMessage;
    }
}
