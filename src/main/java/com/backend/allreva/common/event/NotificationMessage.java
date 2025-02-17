package com.backend.allreva.common.event;

import java.text.MessageFormat;
import java.util.List;
import lombok.Getter;

@Getter
public enum NotificationMessage {

    NEW_NOTIFICATION("새로운 알림 등록", "새로운 알림이 도착했습니다"),
    NEW_RENT_REGISTERED("차량 대절 등록 알림", "새로운 차량 대절 {0}이 등록되었습니다"),
    NEW_SURVEY_REGISTERED("수요 조사 등록 알림", "새로운 수요 조사 {0}이 등록되었습니다"),
    NEW_CHAT_MESSAGE("%s", "%s");

    private final String title;
    private final String message;

    NotificationMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public NotificationEvent toEvent(
            List<Long> recipientIds,
            Object... args
    ){
        return NotificationEvent.builder()
                .title(title)
                .message(MessageFormat.format(message, args))
                .recipientIds(recipientIds)
                .build();
    }
}
