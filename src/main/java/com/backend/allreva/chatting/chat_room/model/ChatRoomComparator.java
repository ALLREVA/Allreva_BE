package com.backend.allreva.chatting.chat_room.model;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ChatRoomComparator implements Comparator<ChatRoom> {

    @Override
    public int compare(ChatRoom o1, ChatRoom o2) {
        LocalDateTime o1SentAt = o1.getPreviewMessage().getSentAt();
        LocalDateTime o2SentAt = o2.getPreviewMessage().getSentAt();

        int comparedValue = o2SentAt.compareTo(o1SentAt);
        if (comparedValue != 0) {
            return comparedValue;
        }

        comparedValue = o1.getRoomId().compareTo(o2.getRoomId());
        if (comparedValue != 0) {
            return comparedValue;
        }
        return o1.getRoomType().compareTo(o2.getRoomType());
    }
}
