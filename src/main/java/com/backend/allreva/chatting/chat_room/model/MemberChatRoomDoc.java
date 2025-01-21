package com.backend.allreva.chatting.chat_room.model;

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
    private SortedSet<ChatRoom> chatRooms = new ConcurrentSkipListSet<>(new ChatRoomComparator());


    public void upsertChatRoom(final ChatRoom chatRoom) {
        this.chatRooms.remove(chatRoom);
        this.chatRooms.add(chatRoom);
    }

    public void deleteChatRoom(final ChatRoom chatRoom) {
        this.chatRooms.remove(chatRoom);
    }

    public void updatePreviewMessage(
            final Long roomId,
            final RoomType roomType,
            final long previewMessageNumber,
            final String previewText,
            final LocalDateTime sentAt
    ) {
        ChatRoom chatRoom = chatRooms.stream()
                .filter(room -> room.getRoomId().equals(roomId) && room.getRoomType().equals(roomType))
                .findFirst()
                .orElseThrow(ChatRoomNotFoundException::new);

        PreviewMessage previewMessage = new PreviewMessage(
                previewMessageNumber,
                previewText,
                sentAt
        );

        chatRooms.remove(chatRoom);
        chatRoom.updatePreviewMessage(previewMessage);
        chatRooms.add(chatRoom);
    }
}
