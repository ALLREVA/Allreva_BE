package com.backend.allreva.chatting.chat.query.model.chat_room;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"roomId", "roomType"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomSummary {

    private Long roomId;
    private RoomType roomType;

    private RoomInfoSummary roomInfoSummary; // title, thumbnail, headcount
    private PreviewMessage previewMessage;

    private long lastReadMessageNumber;

    public ChatRoomSummary(
            final RoomInfoSummary roomInfoSummary,
            final PreviewMessage previewMessage,
            final long lastReadMessageNumber
    ) {
        this.roomInfoSummary = roomInfoSummary;
        this.previewMessage = previewMessage;
        this.lastReadMessageNumber = lastReadMessageNumber;
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
