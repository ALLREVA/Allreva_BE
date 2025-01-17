package com.backend.allreva.notification.command;

import com.backend.allreva.notification.command.dto.NotificationRequest;

public interface NotificationSender {

    void sendMessage(NotificationRequest notificationRequest);
}
