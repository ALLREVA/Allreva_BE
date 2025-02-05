package com.backend.allreva.common.event;

import java.util.List;
import lombok.Builder;

@Builder
public record NotificationEvent(
        List<Long> recipientIds,
        String title,
        String message
) {

}