package com.backend.allreva.notification.infra;

import com.backend.allreva.notification.command.NotificationSender;
import com.backend.allreva.notification.command.dto.NotificationRequest;
import org.springframework.stereotype.Component;

@Component
public class FcmSender implements NotificationSender {

    @Override
    public void sendMessage(final NotificationRequest notificationRequest) {

    }
}
