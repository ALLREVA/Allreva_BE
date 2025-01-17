package com.backend.allreva.notification.command.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record NotificationRequest(
        List<Long> recipientIds,
        String title,
        String body
) {

}
