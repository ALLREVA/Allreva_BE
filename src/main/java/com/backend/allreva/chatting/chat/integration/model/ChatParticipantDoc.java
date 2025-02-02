package com.backend.allreva.chatting.chat.integration.model;

import com.backend.allreva.chatting.chat.integration.model.value.ChatInfoSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import com.backend.allreva.chatting.exception.ChatRoomNotFoundException;
import com.backend.allreva.common.model.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class ChatParticipantDoc {

    @Id
    private Long memberId;
    private SortedSet<ChatSummary> chatSummaries = new ConcurrentSkipListSet<>(new ChatComparator());

    public ChatParticipantDoc(final Long memberId) {
        this.memberId = memberId;
    }

    public void addChatSummary(
            final Long roomId,
            final ChatType chatType,
            final ChatInfoSummary chatInfoSummary
            ) {
        ChatSummary chatSummary = ChatSummary.of(roomId, chatType, chatInfoSummary);
        this.chatSummaries.add(chatSummary);
    }

    public void upsertChatRoom(final ChatSummary chatSummary) {
        this.chatSummaries.remove(chatSummary);
        this.chatSummaries.add(chatSummary);
    }

    public void removeChatRoom(
            final Long roomId,
            final ChatType chatType
    ) {
        ChatSummary chatSummary = ChatSummary.of(roomId, chatType);
        this.chatSummaries.remove(chatSummary);
    }

    public boolean existsInSummaries(
            final Long roomId,
            final ChatType chatType
    ) {
        ChatSummary summary = ChatSummary.of(roomId, chatType);
        return this.chatSummaries.contains(summary);
    }

    public void updatePreviewMessage(
            final Long roomId,
            final ChatType chatType,
            final long previewMessageNumber,
            final String previewText,
            final LocalDateTime sentAt
    ) {
        ChatSummary chatSummary = chatSummaries.stream()
                .filter(room -> room.getChatId().equals(roomId) && room.getChatType().equals(chatType))
                .findFirst()
                .orElseThrow(ChatRoomNotFoundException::new);

        PreviewMessage previewMessage = new PreviewMessage(
                previewMessageNumber,
                previewText,
                sentAt
        );

        chatSummaries.remove(chatSummary);
        chatSummary.updatePreviewMessage(previewMessage);
        chatSummaries.add(chatSummary);
    }

    public void updatePreviewMessage(
            final Long roomId,
            final ChatType chatType,
            final PreviewMessage previewMessage
    ) {
        ChatSummary chatSummary = chatSummaries.stream()
                .filter(room -> room.getChatId().equals(roomId) && room.getChatType().equals(chatType))
                .findFirst()
                .orElseThrow(ChatRoomNotFoundException::new);

        PreviewMessage updatedPreviewMessage = previewMessage;

        chatSummaries.remove(chatSummary);
        chatSummary.updatePreviewMessage(updatedPreviewMessage);
        chatSummaries.add(chatSummary);
    }

    public void updateChatInfoSummary(
            final Long roomId,
            final ChatType chatType,
            final String title,
            final Image thumbnail
    ) {
        ChatSummary chatSummary = chatSummaries.stream()
                .filter(room -> room.getChatId().equals(roomId) && room.getChatType().equals(chatType))
                .findFirst()
                .orElseThrow(ChatRoomNotFoundException::new);

        ChatInfoSummary chatInfoSummary = new ChatInfoSummary(
                title,
                thumbnail,
                chatSummary.getChatInfoSummary().getHeadcount()
        );

        chatSummaries.remove(chatSummary);
        chatSummary.updateChatInfoSummary(chatInfoSummary);
        chatSummaries.add(chatSummary);
    }
}
