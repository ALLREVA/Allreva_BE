package com.backend.allreva.chatting.chat.integration.model.value;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"chatId", "chatType"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatSummary implements Comparable<ChatSummary> {

    private Long chatId;
    private ChatType chatType;

    private ChatInfoSummary chatInfoSummary; // title, thumbnail, headcount
    private PreviewMessage previewMessage;

    private long lastReadMessageNumber;

    public ChatSummary(
            final Long chatId,
            final ChatType chatType,
            final ChatInfoSummary chatInfoSummary,
            final PreviewMessage previewMessage,
            final long lastReadMessageNumber
    ) {
        this.chatId = chatId;
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

    public static ChatSummary of(
            final Long roomId,
            final ChatType chatType
    ) {
        ChatSummary chatSummary = new ChatSummary();
        chatSummary.chatId = roomId;
        chatSummary.chatType = chatType;

        return chatSummary;
    }

    public static ChatSummary of(
            final Long roomId,
            final ChatType chatType,
            final ChatInfoSummary chatInfoSummary
    ) {
        ChatSummary chatSummary = new ChatSummary();

        chatSummary.chatId = roomId;
        chatSummary.chatType = chatType;
        chatSummary.chatInfoSummary = chatInfoSummary;

        return chatSummary;
    }

    public void updatePreviewMessage(final PreviewMessage previewMessage) {
        this.previewMessage = previewMessage;
    }

    public void updateChatInfoSummary(final ChatInfoSummary chatInfoSummary) {
        this.chatInfoSummary = chatInfoSummary;
    }

    public void updateLastReadMessageNumber(final Long lastReadMessageNumber) {
        this.lastReadMessageNumber = lastReadMessageNumber;
    }

    @Override
    public int compareTo(ChatSummary o) {
        LocalDateTime sentAt = getSentAt(this.previewMessage);
        LocalDateTime otherSentAt = getSentAt(o.getPreviewMessage());

        int comparedValue = otherSentAt.compareTo(sentAt);
        if (comparedValue != 0) {
            return comparedValue;
        }

        comparedValue = this.getChatId().compareTo(o.getChatId());
        if (comparedValue != 0) {
            return comparedValue;
        }
        return this.getChatType().compareTo(o.getChatType());
    }

    private LocalDateTime getSentAt(final PreviewMessage previewMessage) {
        if (previewMessage == null) {
            return LocalDateTime.MIN;
        }
        return previewMessage.getSentAt();
    }
}
