package com.backend.allreva.chatting.chat.integration.model;

import com.backend.allreva.chatting.chat.integration.model.value.ChatSummary;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ChatComparator implements Comparator<ChatSummary> {

    @Override
    public int compare(ChatSummary o1, ChatSummary o2) {
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
        return o1.getChatType().compareTo(o2.getChatType());
    }
}
