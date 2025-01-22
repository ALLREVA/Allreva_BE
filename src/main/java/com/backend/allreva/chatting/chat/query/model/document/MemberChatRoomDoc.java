package com.backend.allreva.chatting.chat.query.model.document;

import com.backend.allreva.chatting.chat.query.model.chat_room.ChatRoomSummary;
import com.backend.allreva.chatting.chat.query.model.chat_room.ChatRoomComparator;
import com.backend.allreva.chatting.chat.query.model.chat_room.PreviewMessage;
import com.backend.allreva.chatting.chat.query.model.chat_room.RoomType;
import com.backend.allreva.chatting.exception.ChatRoomNotFoundException;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class MemberChatRoomDoc {

    @Id
    private Long memberId;
    private SortedSet<ChatRoomSummary> chatRoomSummaries = new ConcurrentSkipListSet<>(new ChatRoomComparator());


    public void upsertChatRoom(final ChatRoomSummary chatRoomSummary) {
        this.chatRoomSummaries.remove(chatRoomSummary);
        this.chatRoomSummaries.add(chatRoomSummary);
    }

    public void deleteChatRoom(final ChatRoomSummary chatRoomSummary) {
        this.chatRoomSummaries.remove(chatRoomSummary);
    }

    public void updatePreviewMessage(
            final Long roomId,
            final RoomType roomType,
            final long previewMessageNumber,
            final String previewText,
            final LocalDateTime sentAt
    ) {
        ChatRoomSummary chatRoomSummary = chatRoomSummaries.stream()
                .filter(room -> room.getRoomId().equals(roomId) && room.getRoomType().equals(roomType))
                .findFirst()
                .orElseThrow(ChatRoomNotFoundException::new);

        PreviewMessage previewMessage = new PreviewMessage(
                previewMessageNumber,
                previewText,
                sentAt
        );

        chatRoomSummaries.remove(chatRoomSummary);
        chatRoomSummary.updatePreviewMessage(previewMessage);
        chatRoomSummaries.add(chatRoomSummary);
    }
}
