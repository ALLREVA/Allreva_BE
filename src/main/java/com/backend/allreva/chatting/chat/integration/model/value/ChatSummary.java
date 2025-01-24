package com.backend.allreva.chatting.chat.integration.model.value;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"roomId", "chatType"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatSummary {

    private Long roomId;
    private ChatType chatType;

    private ChatInfoSummary chatInfoSummary; // title, thumbnail, headcount
    private PreviewMessage previewMessage;

    private long lastReadMessageNumber;

    public ChatSummary(
            final Long roomId,
            final ChatType chatType,
            final ChatInfoSummary chatInfoSummary,
            final PreviewMessage previewMessage,
            final long lastReadMessageNumber
    ) {
        this.roomId = roomId;
        this.chatType = chatType;
        this.chatInfoSummary = chatInfoSummary;
        this.previewMessage = previewMessage;
        this.lastReadMessageNumber = lastReadMessageNumber;
    }

    public ChatSummary(
            final ChatInfoSummary chatInfoSummary,
            final PreviewMessage previewMessage,
            final long lastReadMessageNumber
    ) {
        this.chatInfoSummary = chatInfoSummary;
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

    public static ChatSummary of(
            final Long roomId,
            final ChatType chatType
    ) {
        ChatSummary chatSummary = new ChatSummary();
        chatSummary.roomId = roomId;
        chatSummary.chatType = chatType;

        return chatSummary;
    }

    public static ChatSummary of(
            final Long roomId,
            final ChatType chatType,
            final ChatInfoSummary chatInfoSummary
    ) {
        ChatSummary chatSummary = new ChatSummary();

        chatSummary.roomId = roomId;
        chatSummary.chatType = chatType;
        chatSummary.chatInfoSummary = chatInfoSummary;

        return chatSummary;
    }

    public void updatePreviewMessage(final PreviewMessage previewMessage) {
        this.previewMessage = previewMessage;
    }
}
