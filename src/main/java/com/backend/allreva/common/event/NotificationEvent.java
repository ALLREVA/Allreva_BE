package com.backend.allreva.common.event;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationEvent extends Event {
    private final List<Long> recipientIds;
    private final String title;
    private final String message;
}