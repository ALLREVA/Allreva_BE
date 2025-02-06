package com.backend.allreva.common.event;

import java.util.List;
import lombok.Getter;

@Getter
public enum NotificationMessage {

    NEW_NOTIFICATION("새로운 알림 등록", "새로운 알림이 도착했습니다"),
    NEW_CHAT_MESSAGE("%s", "%s");

    private final String title;
    private final String message;

    NotificationMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public NotificationEvent toEvent(List<Long> recipientIds) {
        return NotificationEvent.builder()
                .title(title)
                .message(message)
                .recipientIds(recipientIds)
                .build();
    }
}
